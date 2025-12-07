import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ChoreListComponent } from "./chore-list/chore-list.component";
import { ParentViewComponent } from "./parent-view/parent-view.component";


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HomeComponent, ChoreListComponent, ParentViewComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
