import { Component, Input, inject } from '@angular/core';
import { ParentService } from '../services';
import { ChildService } from '../services';
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';

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

  @Input() chores: any[] = [];
  @Input() mode: 'parent' | 'child' = 'child';

  choreName: string = "";
  choreDescription: string = "";
  chorePrice: number = 1;
  assignedChild: string = "";

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