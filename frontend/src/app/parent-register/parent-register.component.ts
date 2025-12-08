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

  onSubmit() {
    this.parentService.createParent({ name: this.parentName }).subscribe({
      next: (parent) => {
        console.log('Created parent:', parent);
        this.authService.setParent(parent.parentId);
        this.router.navigate(['/parent']);
      },
      error: (err) => console.error('Failed to create parent:', err)
    });
  }
}