import { Component, OnInit, inject } from '@angular/core';
import { ParentService } from '../services/parent.service';
import { ChoreListComponent } from '../chore-list/chore-list.component';
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';
import { StorefrontComponent } from '../storefront/storefront.component';
import { TransactionsComponent } from '../transactions/transactions.component';
import { Child, ChildWithPin, Chore, Transaction } from '../../models';
import { ChildService } from '../services';

@Component({
  selector: 'app-parent-view',
  standalone: true,
  imports: [ChoreListComponent, FormsModule, StorefrontComponent, TransactionsComponent],
  templateUrl: './parent-view.component.html',
  styleUrl: './parent-view.component.css'
})
export class ParentViewComponent implements OnInit {
  private parentService = inject(ParentService);
  private childService = inject(ChildService);
  private authService = inject(AuthService);

  private _activeComponent: 'children' | 'chores' | 'store' | 'transactions' = 'children';

  get activeComponent(){
    return this._activeComponent;
  }

  set activeComponent(value: 'children' | 'chores' | 'store' | 'transactions') {
    if (this._activeComponent !== value) {
      this._activeComponent = value;
      const parentId = this.authService.getParentId();
      if (parentId) {
        this.refreshComponentData(parentId);
      }
    }
  }
  children: Child[] = [];
  chores: Chore[] = [];
  transactions: Transaction[] = [];

  // New child form
  newChildName: string = '';
  newChildUsername: string = '';

  // Edit child state
  editingChild: Child | null = null;
  editChildName: string = '';
  editChildUsername: string = '';

  // Modal state
  showAllowanceModal: boolean = false;
  showTransactionsModal: boolean = false;
  showChoresModal: boolean = false;
  showPinModal: boolean = false;
  showNewChildPinModal: boolean = false;
  showNewChildAccountModal: boolean = false;
  selectedChild: Child | null = null;
  allowanceAmount: number = 0;
  childTransactions: Transaction[] = [];
  childChores: Chore[] = [];
  currentPin: string = '';
  newChildPin: string = '';
  newChildCreated: ChildWithPin | null = null;

  ngOnInit() {
    const parentId = this.authService.getParentId();

    if (!parentId) {
      console.error('No parent logged in');
      return;
    }

    this.loadChildren(parentId);
    this.loadChores(parentId);
    this.loadTransactions(parentId);
  }

  loadChildren(parentId: string) {
    this.parentService.getChildren(parentId).subscribe({
      next: (data) => this.children = data,
      error: (err) => console.error('Failed to load children:', err)
    });
  }

  loadChores(parentId: string) {
    this.parentService.getChores(parentId).subscribe({
      next: (data) => this.chores = data,
      error: (err) => console.error('Failed to load chores:', err)
    });
  }

  loadTransactions(parentId: string) {
    this.parentService.getTransactions(parentId).subscribe({
      next: (data) => this.transactions = data,
      error: (err) => console.error('Failed to load transactions:', err)
    });
  }


  refreshComponentData(parentId: string) {
    switch(this.activeComponent) {
      case 'children':
        this.loadChildren(parentId);
        break;
      case 'chores':
        this.loadChores(parentId);
        break;
      case 'transactions':
        this.loadTransactions(parentId);
    }
  }

  closeNewChildPinModal() {
    this.showNewChildPinModal = false;
    this.newChildCreated = null;
    this.newChildPin = '';
  }

  startChildEdit(child: Child) {
    this.editingChild = child;
    this.editChildName = child.name;
    this.editChildUsername = child.username;
  }

  cancelChildEdit() {
    this.editingChild = null;
    this.editChildName = '';
    this.editChildUsername = '';
  }

  saveChildEdit() {
    const parentId = this.authService.getParentId();
    if (!parentId || !this.editingChild) return;

    this.parentService.updateChild(parentId, this.editingChild.childId, {
      name: this.editChildName,
      username: this.editChildUsername
    }).subscribe({
      next: (updatedChild) => {
        const index = this.children.findIndex(c => c.childId === updatedChild.childId);
        if (index !== -1) {
          this.children[index] = updatedChild;
        }
        this.cancelChildEdit();
        this.loadChildren(parentId);
      },
      error: (err) => console.error('Failed to update child:', err)
    });
  }

  // Allowance Modal Methods
  openAllowanceModal(child: Child) {
    this.selectedChild = child;
    this.allowanceAmount = 0;
    this.showAllowanceModal = true;
  }

  closeAllowanceModal() {
    this.showAllowanceModal = false;
    this.selectedChild = null;
    this.allowanceAmount = 0;
  }

  sendAllowance() {
    const parentId = this.authService.getParentId();
    if (!parentId || !this.selectedChild || this.allowanceAmount <= 0) return;

    this.parentService.addAllowance(parentId, this.selectedChild.childId, this.allowanceAmount).subscribe({
      next: (updatedChild) => {
        const index = this.children.findIndex(c => c.childId === updatedChild.childId);
        if (index !== -1) {
          this.children[index] = updatedChild;
        }
        this.closeAllowanceModal();
        // Reload transactions
        this.loadChildren(parentId);
        this.loadTransactions(parentId);
      },
      error: (err) => console.error('Failed to send allowance:', err)
    });
  }

  // Transactions Modal Methods
  viewChildTransactions(child: Child) {
    this.selectedChild = child;
    this.childService.getChild(child.childId).subscribe({
      next: (updatedChild) => {
        this.selectedChild = updatedChild;
        const index = this.children.findIndex(c => c.childId === updatedChild.childId);
        if (index !== -1) {
          this.children[index] = updatedChild;
        }
      }
    });
    this.childService.getTransactions(child.childId).subscribe({
      next: (transactions) => {
        this.childTransactions = transactions;
        this.showTransactionsModal = true;
      },
      error: (err) => console.error('Failed to load child transactions:', err)
    });
  }

  closeTransactionsModal() {
    this.showTransactionsModal = false;
    this.selectedChild = null;
    this.childTransactions = [];
  }

  formatDate(timestamp: string): string {
    const date = new Date(timestamp);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  // Chores Modal Methods
  viewChildChores(child: Child) {
    const parentId = this.authService.getParentId();
    if (!parentId) return;

    this.selectedChild = child;

    this.parentService.getChores(parentId).subscribe({
      next: (chores) => {
        this.chores = chores;
        this.childChores = chores.filter(c => c.assignedChildId === child.childId);
        this.showChoresModal = true;
      },
      error: (err) => console.error("Failed to load chores:", err)
    });
  }

  closeChoresModal() {
    this.showChoresModal = false;
    this.selectedChild = null;
    this.childChores = [];
  }

  approveChoreFromModal(choreId: string) {
    const parentId = this.authService.getParentId();
    if (!parentId) return;

    this.parentService.approveChore(parentId, choreId).subscribe({
      next: (transaction) => {
        console.log('Chore approved:', transaction);

        // Refresh chores
        this.loadChores(parentId);

        // Refresh child data to update balance
        if (this.selectedChild) {
          this.childService.getChild(this.selectedChild.childId).subscribe({
            next: (updatedChild) => {
              const index = this.children.findIndex(c => c.childId === updatedChild.childId);
              if (index !== -1) {
                this.children[index] = updatedChild;
              }
              this.selectedChild = updatedChild;

              // Update childChores list
              this.childChores = this.chores.filter(c => c.assignedChildId === updatedChild.childId);
            }
          });
        }

        // Refresh transactions
        this.loadTransactions(parentId);
      },
      error: (err) => console.error('Failed to approve chore:', err)
    });
  }

  denyChoreFromModal(choreId: string) {
    const parentId = this.authService.getParentId();
    if (!parentId) return;

    this.parentService.denyChore(parentId, choreId).subscribe({
      next: (updatedChore) => {
        console.log('Chore denied:', updatedChore);

        // Refresh chores from server
        this.loadChores(parentId);

        // Update childChores list (remove denied chore)
        this.childChores = this.childChores.filter(c => c.choreId !== choreId);
      },
      error: (err) => console.error('Failed to deny chore:', err)
    });
  }

  // PIN Modal Methods
  viewChildPin(child: Child) {
    const parentId = this.authService.getParentId();
    if (!parentId) return;

    this.selectedChild = child;
    this.parentService.getChildPin(parentId, child.childId).subscribe({
      next: (response) => {
        this.currentPin = response.pin;
        this.showPinModal = true;
      },
      error: (err) => console.error('Failed to get PIN:', err)
    });
  }

  regeneratePin() {
    const parentId = this.authService.getParentId();
    if (!parentId || !this.selectedChild) return;

    if (confirm('Are you sure you want to regenerate the PIN? The old PIN will no longer work.')) {
      this.parentService.regenerateChildPin(parentId, this.selectedChild.childId).subscribe({
        next: (response) => {
          this.currentPin = response.pin;
        },
        error: (err) => console.error('Failed to regenerate PIN:', err)
      });
    }
  }

  closePinModal() {
    this.showPinModal = false;
    this.selectedChild = null;
    this.currentPin = '';
  }

  openNewChildAccountModal() {
    this.showNewChildAccountModal = true;
  }

  closeNewChildAccountModal() {
    this.showNewChildAccountModal = false;
    this.newChildName = "";
    this.newChildUsername = "";
  }

  createChildOnSubmit() {
    const parentId = this.authService.getParentId();

    if (!parentId) {
      console.error('No parent logged in');
      return;
    }

    this.parentService.createChild(parentId, {
      name: this.newChildName,
      username: this.newChildUsername
    }).subscribe({
      next: (child) => {
        console.log('Created child:', child);
        this.closeNewChildAccountModal();

        // Refresh children list
        this.loadChildren(parentId);

        // Show the PIN modal with the new child's credentials
        this.newChildCreated = child;
        this.newChildPin = child.pin;
        this.showNewChildPinModal = true;
      },
      error: (err) => console.error('Failed to create child:', err)
    });
  }
}