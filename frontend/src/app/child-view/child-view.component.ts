import { Component, inject, OnInit } from '@angular/core';
import { ChildService } from '../services';
import { ChoreListComponent } from '../chore-list/chore-list.component';
import { StorefrontComponent } from '../storefront/storefront.component';
import { TransactionsComponent } from '../transactions/transactions.component';
import { AuthService } from '../services/auth.service';
import { Child, Chore, StoreItem, Transaction } from '../../models';

@Component({
  selector: 'app-child-view',
  standalone: true,
  imports: [ChoreListComponent, StorefrontComponent, TransactionsComponent],
  templateUrl: './child-view.component.html',
  styleUrl: './child-view.component.css'
})
export class ChildViewComponent implements OnInit {
  private childService = inject(ChildService);
  private authService = inject(AuthService);

  private _activeComponent: 'account' | 'chores' | 'store' | 'history' = 'account';

  get activeComponent(){
    return this._activeComponent;
  }

  set activeComponent(value: 'account' | 'chores' | 'store' | 'history') {
    if (this._activeComponent !== value) {
      this._activeComponent = value;
      const childId = this.authService.getChildId();
      if (childId) {
        this.refreshComponentData(childId);
      }
    }
  }

  child: Child | null = null;
  chores: Chore[] = [];
  storeItems: StoreItem[] = [];
  transactions: Transaction[] = [];

  get completedChoresCount(): number {
    return this.chores.filter(c => c.status === 'COMPLETED').length;
  }

  get pendingChoresCount(): number {
    return this.chores.filter(c => c.status === 'PENDING').length;
  }

  get purchaseCount(): number {
    return this.transactions.filter(t => t.type === 'PURCHASE').length;
  }

  ngOnInit() {
    const childId = this.authService.getChildId();

    if (!childId) {
      console.error('No child logged in');
      return;
    }

    this.loadChildData(childId);
  }

  loadAllData(childId: string) {
    this.loadChildData(childId);
    this.loadChores(childId);
    this.loadStoreItems(childId);
    this.loadTransactions(childId);
  }

  refreshComponentData(childId: string) {
    this.loadChildData(childId);
    switch (this.activeComponent) {
      case 'account':
        this.loadTransactions(childId);
        this.loadChores(childId);
        break;
      case 'chores':
        this.loadChores(childId);
        break;
      case 'store':
        this.loadStoreItems(childId);
        break;
      case 'history':
        this.loadTransactions(childId);
        break;
    }
  }

  loadChildData(childId: string) {
    this.childService.getChild(childId).subscribe({
      next: (data) => this.child = data,
      error: (err) => console.error('Failed to load child data:', err),
    });
  }

  loadChores(childId: string) {
    this.childService.getAvailableChores(childId).subscribe({
      next: (data) => this.chores = data,
      error: (err) => console.error('Failed to load chore data:', err),
    });
  }

  loadStoreItems(childId: string) {
    this.childService.getStoreItems(childId).subscribe({
      next: (data) => this.storeItems = data,
      error: (err) => console.error('Failed to load store data:', err),
    });
  }

  loadTransactions(childId: string) {
    this.childService.getTransactions(childId).subscribe({
      next: (data) => this.transactions = data,
      error: (err) => console.error('Failed to load transactions:', err),
    });
  }
}