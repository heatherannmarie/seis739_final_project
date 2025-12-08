import { Component, OnInit, inject } from '@angular/core';
import { ChildService } from '../services';
import { ParentService } from '../services';
import { AuthService } from '../services/auth.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-storefront',
  imports: [FormsModule],
  templateUrl: './storefront.component.html',
  styleUrl: './storefront.component.css'
})
export class StorefrontComponent implements OnInit {
  // Displays the store items for both kids and parents, but only parents have rights to edit it

  private childService = inject(ChildService);
  private parentService = inject(ParentService);
  private authService = inject(AuthService);

  inventory: any[] = [];
  itemName: string = "";
  itemPrice: number = 0;
  itemQuantity: number = 1;

  ngOnInit() {
    this.childService.getStoreItems("child_1764733929203").subscribe({
      next: (data) => this.inventory = data,
      error: (err) => console.error("Failed to load inventory:", err)
    })
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
}
