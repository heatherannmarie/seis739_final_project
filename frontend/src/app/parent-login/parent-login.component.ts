import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ParentService } from '../services/parent.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-parent-login',
  imports: [FormsModule],
  templateUrl: './parent-login.component.html',
  styleUrl: './parent-login.component.css'
})
export class ParentLoginComponent {
  private parentService = inject(ParentService);
  private authService = inject(AuthService);
  private router = inject(Router);

  username: string = '';
  errorMessage: string = '';

  onSubmit() {
    if (!this.username.trim()) {
      this.errorMessage = 'Username is required';
      return;
    }

    this.errorMessage = '';

    this.parentService.login(this.username).subscribe({
      next: (parent) => {
        console.log('Logged in as:', parent);
        this.authService.setParent(parent.parentId);
        this.router.navigate(['/parent']);
      },
      error: (err) => {
        console.error('Login failed:', err);
        if (err.error?.error) {
          this.errorMessage = err.error.error;
        } else {
          this.errorMessage = 'Invalid username';
        }
      }
    });
  }
}