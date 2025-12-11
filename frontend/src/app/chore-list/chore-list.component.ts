import { Component, Input, inject } from '@angular/core';
import { ParentService } from '../services';
import { ChildService } from '../services';
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';
import { Chore, Child } from '../../models';
import { OnInit } from '@angular/core';

@Component({
  selector: 'app-chore-list',
  imports: [FormsModule],
  templateUrl: './chore-list.component.html',
  styleUrl: './chore-list.component.css'
})
export class ChoreListComponent implements OnInit {
  private parentService = inject(ParentService);
  private childService = inject(ChildService);
  private authService = inject(AuthService);

  @Input() chores: Chore[] = [];
  @Input() mode: 'parent' | 'child' = 'child';



  // Add chore form fields
  choreName: string = "";
  choreDescription: string = "";
  chorePrice: number = 1;
  assignedChild: string = "";
  availableChildren: Child[] = [];

  // Edit chore state
  editingChore: Chore | null = null;
  editChoreName: string = "";
  editChoreDescription: string = "";
  editChorePrice: number = 0;
  editChoreAssignedChild: string = "";

  showAddChoreModal: boolean = false;

  openAddChoreModal() {
    this.showAddChoreModal = true;
  }

  closeAddChoreModal() {
    this.showAddChoreModal = false;
    this.choreName = "";
    this.choreDescription = "";
    this.chorePrice = 1;
    this.assignedChild = "";
  }

  ngOnInit() {
    // Add this if in parent mode
    if (this.mode === 'parent') {
      const parentId = this.authService.getParentId();
      if (parentId) {
        this.parentService.getChildren(parentId).subscribe({
          next: (children) => this.availableChildren = children,
          error: (err) => console.error('Failed to load children:', err)
        });
      }
    }
  }

  // Start editing a chore
  startChoreEdit(chore: Chore) {
    this.editingChore = chore;
    this.editChoreName = chore.choreName;
    this.editChoreDescription = chore.choreDescription;
    this.editChorePrice = chore.chorePrice;
    this.editChoreAssignedChild = chore.assignedChildId || "";
  }

  // Cancel editing
  cancelChoreEdit() {
    this.editingChore = null;
    this.editChoreName = "";
    this.editChoreDescription = "";
    this.editChorePrice = 0;
    this.editChoreAssignedChild = "";
  }

  // Save chore edits
  saveChoreEdit() {
    const parentId = this.authService.getParentId();
    if (!parentId || !this.editingChore) return;

    this.parentService.updateChore(parentId, this.editingChore.choreId, {
      choreName: this.editChoreName,
      choreDescription: this.editChoreDescription,
      chorePrice: this.editChorePrice,
      assignedChildId: this.editChoreAssignedChild || undefined
    }).subscribe({
      next: (updatedChore) => {
        // Refresh chores from server
        this.parentService.getChores(parentId).subscribe({
          next: (chores) => {
            this.chores = chores;
          }
        });
        this.cancelChoreEdit();
      },
      error: (err) => console.error('Failed to update chore:', err)
    });
  }

  // Delete a chore
  deleteChore(choreId: string) {
    const parentId = this.authService.getParentId();
    if (!parentId) return;

    if (confirm('Are you sure you want to delete this chore?')) {
      this.parentService.deleteChore(parentId, choreId).subscribe({
        next: () => {
          // Refresh chores from server
          this.parentService.getChores(parentId).subscribe({
            next: (chores) => {
              this.chores = chores;
            }
          });
          this.cancelChoreEdit();
        },
        error: (err) => console.error('Failed to delete chore:', err)
      });
    }
  }

  requestCompletion(choreId: string) {
    const childId = this.authService.getChildId();

    if (!childId) {
      console.error('No child logged in');
      return;
    }

    this.childService.requestChoreCompletion(childId, choreId).subscribe({
      next: (updatedChore) => {
        console.log('Requested completion:', updatedChore);
        // Update the local chore status
        const chore = this.chores.find(c => c.choreId === choreId);
        if (chore) {
          chore.status = updatedChore.status;
          chore.assignedChildId = updatedChore.assignedChildId;
        }
      },
      error: (err) => console.error('Failed to request completion:', err)
    });
  }

  approveChore(choreId: string) {
    const parentId = this.authService.getParentId();

    if (!parentId) {
      console.error('No parent logged in');
      return;
    }

    this.parentService.approveChore(parentId, choreId).subscribe({
      next: (transaction) => {
        console.log('Chore approved, transaction:', transaction);

        // Refresh chores from server
        this.parentService.getChores(parentId).subscribe({
          next: (chores) => {
            this.chores = chores;
          }
        });
      },
      error: (err) => console.error('Failed to approve chore:', err)
    });
  }

  denyChore(choreId: string) {
    const parentId = this.authService.getParentId();

    if (!parentId) {
      console.error('No parent logged in');
      return;
    }

    this.parentService.denyChore(parentId, choreId).subscribe({
      next: (updatedChore) => {
        console.log('Chore denied:', updatedChore);
        // Update the local chore status
        const chore = this.chores.find(c => c.choreId === choreId);
        if (chore) {
          chore.status = updatedChore.status;
          chore.assignedChildId = null;
        }
      },
      error: (err) => console.error('Failed to deny chore:', err)
    });
  }

  onSubmit() {
    const parentId = this.authService.getParentId();

    if (!parentId) {
      console.error('No parent logged in');
      return;
    }

    this.parentService.addChore(parentId, {
      choreName: this.choreName,
      choreDescription: this.choreDescription,
      chorePrice: this.chorePrice,
      assignedChildId: this.assignedChild
    }).subscribe({
      next: (item) => {
        console.log("Creating chore:", item);

        // Refresh chores from server to get complete list
        this.parentService.getChores(parentId).subscribe({
          next: (chores) => {
            this.chores = chores;
          }
        });

        this.closeAddChoreModal();
      },
      error: (err) => console.error("Failed to create chore:", err)
    });
  }
}