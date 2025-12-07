import { Component } from '@angular/core';

@Component({
  selector: 'app-child-view',
  imports: [],
  templateUrl: './child-view.component.html',
  styleUrl: './child-view.component.css'
})
export class ChildViewComponent {
  // Dashboard for child accounts
  child = {
    name: 'Freddie',
    balance: 47,
    username: 'freddie123'
  };

  recentTransactions = [
    { description: 'Completed: Take out trash', amount: 5, date: '2024-12-05', type: 'earned' },
    { description: 'Purchased: Extra Screen Time', amount: -10, date: '2024-12-04', type: 'spent' },
    { description: 'Completed: Do dishes', amount: 10, date: '2024-12-03', type: 'earned' },
    { description: 'Completed: Vacuum living room', amount: 15, date: '2024-12-01', type: 'earned' },
    { description: 'Purchased: Movie Night Pick', amount: -20, date: '2024-11-28', type: 'spent' },
  ];

  availableChores = [
    { name: 'Clean bathroom', points: 20 },
    { name: 'Rake leaves', points: 15 },
    { name: 'Fold laundry', points: 10 },
  ];

}
