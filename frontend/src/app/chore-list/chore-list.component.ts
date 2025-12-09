import { Component, Input, inject } from '@angular/core';
import { ParentService } from '../services';
import { ChildService } from '../services';
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';
import { Chore } from '../../models';

@Component({
  selector: 'app-chore-list',
  imports: [FormsModule],
  templateUrl: './chore-list.component.html',
  styleUrl: './chore-list.component.css'
})
export class ChoreListComponent {
  private parentService = inject(ParentService);
  private childService = inject(ChildService);
  private authService = inject(AuthService);

  @Input() chores: Chore[] = [];
  @Input() mode: 'parent' | 'child' = 'child';

  choreName: string = "";
  choreDescription: string = "";
  chorePrice: number = 1;
  assignedChild: string = "";

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
        // Update the local chore status
        const chore = this.chores.find(c => c.choreId === choreId);
        if (chore) {
          chore.status = 'COMPLETED' as any;
        }
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
        this.chores.push(item);
        this.choreName = "";
        this.choreDescription = "";
        this.chorePrice = 1;
        this.assignedChild = "";
      },
      error: (err) => console.error("Failed to create chore:", err)
    });
  }
}