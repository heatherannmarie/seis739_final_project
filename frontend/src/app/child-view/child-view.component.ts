import { Component, inject, OnInit } from '@angular/core';
import { ChildService } from '../services';
import { ChoreListComponent } from '../chore-list/chore-list.component';
import { RouterLink } from '@angular/router';
import { StorefrontComponent } from '../storefront/storefront.component';
import { AuthService } from '../services/auth.service';


@Component({
  selector: 'app-child-view',
  imports: [ChoreListComponent, StorefrontComponent],
  templateUrl: './child-view.component.html',
  styleUrl: './child-view.component.css'
})
export class ChildViewComponent implements OnInit {
  // Dashboard for child accounts
  private childService = inject(ChildService);
  private authService = inject(AuthService);

  activeSection: 'account' | 'chores' | 'store' = 'account';
  child: any;
  chores: any[] = [];
  storeItems: any[] = [];
  transactions: any[] = [];

  ngOnInit() {
    const childId = this.authService.getChildId();

    if (!childId) {
      console.error('No child logged in');
      return;
    }

    this.childService.getChild(childId).subscribe({
      next: (data) => this.child = data,
      error: (err) => console.error("Failed to load child data:", err),
    })

    this.childService.getAvailableChores("child_1764733929203").subscribe({
      next: (data) => this.chores = data,
      error: (err) => console.error("Failed to load chore data:", err),
    })

    this.childService.getStoreItems("child_1764733929203").subscribe({
      next: (data) => this.storeItems = data,
      error: (err) => console.error("Failed to load store data:", err),
    })

    this.childService.getTransactions("child_1764733929203").subscribe({
      next: (data) => this.transactions = data,
      error: (err) => console.error("Failed to load transactions:", err),
    })
  }

}
