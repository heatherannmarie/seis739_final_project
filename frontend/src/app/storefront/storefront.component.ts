import { Component } from '@angular/core';

@Component({
  selector: 'app-storefront',
  imports: [],
  templateUrl: './storefront.component.html',
  styleUrl: './storefront.component.css'
})
export class StorefrontComponent {
  // Displays the store items for both kids and parents, but only parents have rights to edit it
  inventory = [
    { name: '30 Minutes Extra Screen Time', price: 10, quantity: 99 },
    { name: 'Pick Dinner for the Family', price: 25, quantity: 5 },
    { name: 'Skip One Chore', price: 15, quantity: 3 },
    { name: 'Movie Night Pick', price: 20, quantity: 10 },
    { name: 'Stay Up 30 Minutes Late', price: 12, quantity: 99 },
    { name: '$5 Cash', price: 50, quantity: 99 },
    { name: 'Trip to Ice Cream Shop', price: 35, quantity: 2 },
    { name: 'New Book of Choice', price: 40, quantity: 5 },
  ];

}
