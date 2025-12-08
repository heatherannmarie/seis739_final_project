import { Router, RouterOutlet, RouterLink } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from './services/auth.service';

export class AppComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
