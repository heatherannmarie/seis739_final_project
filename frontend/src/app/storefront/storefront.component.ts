import { Component, OnInit, inject } from '@angular/core';
import { ChildService } from '../services';
import { ParentService } from '../services';
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';
import { StoreItem } from '../../models';

@Component({
  selector: 'app-storefront',
  imports: [FormsModule],
  templateUrl: './storefront.component.html',
  styleUrl: './storefront.component.css'
})
export class StorefrontComponent implements OnInit {
  private childService = inject(ChildService);
  private parentService = inject(ParentService);
  private authService = inject(AuthService);

  inventory: StoreItem[] = [];

  // Add item form fields
  itemName: string = "";
  itemPrice: number = 0;
  itemQuantity: number = 1;

  // Edit item state
  editingItem: StoreItem | null = null;
  editItemName: string = "";
  editItemPrice: number = 0;
  editItemQuantity: number = 0;

  isParent: boolean = false;

  ngOnInit() {
    const parentId = this.authService.getParentId();
    const childId = this.authService.getChildId();

    if (parentId) {
      this.isParent = true;
      this.parentService.getStoreItems(parentId).subscribe({
        next: (data) => this.inventory = data,
        error: (err) => console.error("Failed to load inventory:", err)
      });
    } else if (childId) {
      this.isParent = false;
      this.childService.getStoreItems(childId).subscribe({
        next: (data) => this.inventory = data,
        error: (err) => console.error("Failed to load inventory:", err)
      });
    }
  }

  // Start editing an item
  startItemEdit(item: StoreItem) {
    this.editingItem = item;
    this.editItemName = item.itemName;
    this.editItemPrice = item.itemPrice;
    this.editItemQuantity = item.availableInventory;
  }

  // Cancel editing
  cancelItemEdit() {
    this.editingItem = null;
    this.editItemName = "";
    this.editItemPrice = 0;
    this.editItemQuantity = 0;
  }

  // Save item edits
  saveItemEdit() {
    const parentId = this.authService.getParentId();
    if (!parentId || !this.editingItem) return;

    this.parentService.updateStoreItem(parentId, this.editingItem.itemID, {
      itemName: this.editItemName,
      itemPrice: this.editItemPrice,
      availableInventory: this.editItemQuantity
    }).subscribe({
      next: (updatedItem) => {
        const index = this.inventory.findIndex(i => i.itemID === updatedItem.itemID);
        if (index !== -1) {
          this.inventory[index] = updatedItem;
        }
        this.cancelItemEdit();
      },
      error: (err) => console.error('Failed to update item:', err)
    });
  }

  // Delete an item
  deleteItem(itemId: string) {
    const parentId = this.authService.getParentId();
    if (!parentId) return;

    if (confirm('Are you sure you want to delete this item?')) {
      this.parentService.deleteStoreItem(parentId, itemId).subscribe({
        next: () => {
          this.inventory = this.inventory.filter(i => i.itemID !== itemId);
          this.cancelItemEdit();
        },
        error: (err) => console.error('Failed to delete item:', err)
      });
    }
  }

  onSubmit() {
    const parentId = this.authService.getParentId();

    if (!parentId) {
      console.error('No parent logged in');
      return;
    }

    this.parentService.addStoreItem(parentId, {
      itemName: this.itemName,
      itemPrice: this.itemPrice,
      availableInventory: this.itemQuantity
    }).subscribe({
      next: (item) => {
        console.log('Created item:', item);
        this.inventory.push(item);
        this.itemName = '';
        this.itemPrice = 0;
        this.itemQuantity = 1;
      },
      error: (err) => console.error('Failed to create item:', err)
    });
  }

  purchaseItem(itemId: string) {
    const childId = this.authService.getChildId();

    if (!childId) {
      console.error('No child logged in');
      return;
    }

    this.childService.purchaseItem(childId, itemId).subscribe({
      next: (transaction) => {
        console.log('Purchase successful:', transaction);
        const item = this.inventory.find(i => i.itemID === itemId);
        if (item) {
          item.availableInventory--;
        }
      },
      error: (err) => console.error('Purchase failed:', err)
    });
  }
}