import { Component, OnInit, inject } from '@angular/core';
import { ParentService } from '../services/parent.service';
import { ChoreListComponent } from "../chore-list/chore-list.component";
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { StorefrontComponent } from '../storefront/storefront.component';

@Component({
  selector: 'app-parent-view',
  imports: [ChoreListComponent, FormsModule, StorefrontComponent],
  templateUrl: './parent-view.component.html',
  styleUrl: './parent-view.component.css'
})
export class ParentViewComponent implements OnInit {
  private parentService = inject(ParentService);
  private authService = inject(AuthService)

  activeSection: 'children' | 'chores' | 'store' = 'children';
  children: any[] = [];
  chores: any[] = [];
  newChildName: string = '';
  newChildUsername: string = '';

  ngOnInit() {
    const parentId = this.authService.getParentId();

    if (!parentId) {
      console.error('No parent logged in');
      return;
    }

    this.parentService.getChildren(parentId).subscribe({
      next: (data) => this.children = data,
      error: (err) => console.error('Failed to load children:', err)
    });

    this.parentService.getChores(parentId).subscribe({
      next: (data) => this.chores = data,
      error: (err) => console.error('Failed to load chores:', err)
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
        this.children.push(child);  // Add to list without refetching
        this.newChildName = '';     // Clear form
        this.newChildUsername = '';
      },
      error: (err) => console.error('Failed to create child:', err)
    });
  }
}