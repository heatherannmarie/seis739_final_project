import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { ChoreListComponent } from "../chore-list/chore-list.component";

@Component({
  selector: 'app-parent-view',
  imports: [ChoreListComponent],
  templateUrl: './parent-view.component.html',
  styleUrl: './parent-view.component.css'
})
export class ParentViewComponent {
  // Dashboard for parent accounts

  children = [
    { name: "Freddie", totalAllowance: 20 },
    { name: "Squirtle", totalAllowance: 8 },
    { name: "Quetie", totalAllowance: 13},
  ]

  
}
