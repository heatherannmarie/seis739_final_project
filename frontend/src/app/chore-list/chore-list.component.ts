import { Component } from '@angular/core';

@Component({
  selector: 'app-chore-list',
  imports: [],
  templateUrl: './chore-list.component.html',
  styleUrl: './chore-list.component.css'
})
export class ChoreListComponent {
  // Needs to display list of chores and allow kids to select them for themselves, as well as allow parents to edit aspects of the chores

  chores = [
    { name: 'Take out trash', points: 5, assignedTo: '' },
    { name: 'Do dishes', points: 10, assignedTo: '' },
    { name: 'Vacuum living room', points: 15, assignedTo: '' },
    { name: 'Feed the dog', points: 5, assignedTo: '' },
  ];

}
