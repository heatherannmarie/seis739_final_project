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

  username: string = '';
  pin: string = '';
  errorMessage: string = '';

  onSubmit() {
    if (!this.username.trim()) {
      this.errorMessage = 'Username is required';
      return;
    }
    if (!this.pin.trim()) {
      this.errorMessage = 'PIN is required';
      return;
    }
    if (!/^\d{6}$/.test(this.pin)) {
      this.errorMessage = 'PIN must be exactly 6 digits';
      return;
    }

    this.errorMessage = '';

    this.childService.login(this.username, this.pin).subscribe({
      next: (child) => {
        console.log('Logged in as:', child);
        this.authService.setChild(child.childId);
        this.router.navigate(['/child']);
      },
      error: (err) => {
        console.error('Login failed:', err);
        if (err.error?.error) {
          this.errorMessage = err.error.error;
        } else {
          this.errorMessage = 'Invalid username or PIN';
        }
      }
    });
  }
}