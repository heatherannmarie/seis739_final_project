import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ChildService } from '../services';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-child-login',
  imports: [FormsModule],
  templateUrl: './child-login.component.html',
  styleUrl: './child-login.component.css'
})
export class ChildLoginComponent {
  private childService = inject(ChildService);
  private authService = inject(AuthService);
  private router = inject(Router);

  childId: string = "";
  errorMessage: string = "";

  onSubmit() {
    this.childService.getChild(this.childId).subscribe({
      next: (child) => {
        console.log("Logged in as:", child);
        this.authService.setChild(child.childId);
        this.router.navigate(["/child"]);
      },
      error: (err) => {
        console.error("Login failed:", err);
        this.errorMessage = "Child not found";
      }
    })
  }

}
