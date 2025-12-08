import { Component, OnInit, inject } from '@angular/core';
import { ParentService } from '../services/parent.service';
import { ChoreListComponent } from '../chore-list/chore-list.component';
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';
import { StorefrontComponent } from '../storefront/storefront.component';
import { TransactionsComponent } from '../transactions/transactions.component';
import { Child, Chore, Transaction } from '../../models';

@Component({
  selector: 'app-parent-view',
  standalone: true,
  imports: [ChoreListComponent, FormsModule, StorefrontComponent, TransactionsComponent],
  template: "./parent-view.component.html",
  styleUrl: './parent-view.component.css'
})
export class ParentViewComponent implements OnInit {
  private parentService = inject(ParentService);
  private authService = inject(AuthService);

  activeSection: 'children' | 'chores' | 'store' | 'transactions' = 'children';
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
        this.children.push(child);
        this.newChildName = '';
        this.newChildUsername = '';
      },
      error: (err) => console.error('Failed to create child:', err)
    });
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
      },
      error: (err) => console.error('Failed to update child:', err)
    });
  }
}