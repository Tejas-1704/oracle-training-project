import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet, RouterLink, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { ApiService } from './api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  showNav = false;
  showProfile = false;
  userId = '';
  username = '';
  userInitials = '';
  customerIdHint = '';

  constructor(private router: Router, private api: ApiService) {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        this.showNav = !['/login', '/register'].includes(event.urlAfterRedirects);
        const user = this.api.getUser();
        this.userId = user?.customerId || user?.id || '';
        this.username = user?.username || '';
        this.userInitials = (this.username || 'U').slice(0, 2).toUpperCase();
        this.customerIdHint = this.userId ? `ID: ${this.userId}` : '';
        this.showProfile = false;
      });
  }

  toggleProfile() {
    this.showProfile = !this.showProfile;
  }

  logout(event: Event) {
    event.stopPropagation();
    this.api.clearAuth();
    this.router.navigate(['/login']);
  }
}
