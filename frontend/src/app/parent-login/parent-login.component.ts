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

  parentId: string = '';
  errorMessage: string = '';

  onSubmit() {
    this.parentService.getParent(this.parentId).subscribe({
      next: (parent) => {
        console.log('Logged in as:', parent);
        this.authService.setParent(parent.parentId);
        this.router.navigate(['/parent']);
      },
      error: (err) => {
        console.error('Login failed:', err);
        this.errorMessage = 'Parent not found';
      }
    });
  }
}