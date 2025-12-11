import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ParentService } from '../services/parent.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-parent-register',
  imports: [FormsModule],
  templateUrl: './parent-register.component.html',
  styleUrl: './parent-register.component.css'
})
export class ParentRegisterComponent {
  private parentService = inject(ParentService);
  private authService = inject(AuthService);
  private router = inject(Router);

  parentName: string = '';
  parentEmail: string = '';
  parentUsername: string = '';
  errorMessage: string = '';

  onSubmit() {
    if (!this.parentName.trim()) {
      this.errorMessage = 'Name is required';
      return;
    }
    if (!this.parentEmail.trim()) {
      this.errorMessage = 'Email is required';
      return;
    }
    if (!this.parentUsername.trim()) {
      this.errorMessage = 'Username is required';
      return;
    }

    this.errorMessage = '';

    this.parentService.createParent({
      name: this.parentName,
      email: this.parentEmail,
      username: this.parentUsername
    }).subscribe({
      next: (parent) => {
        console.log('Created parent:', parent);
        this.authService.setParent(parent.parentId);
        this.router.navigate(['/parent']);
      },
      error: (err) => {
        console.error('Failed to create parent:', err);
        if (err.error?.error) {
          this.errorMessage = err.error.error;
        } else {
          this.errorMessage = 'Failed to create account. Please try again.';
        }
      }
    });
  }
}