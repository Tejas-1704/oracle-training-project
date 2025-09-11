import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../api.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';

  constructor(private api: ApiService, private router: Router) {}

  login() {
    this.api.login({ username: this.username, password: this.password }).subscribe({
      next: (user: any) => {
        this.api.setAuth(this.username, this.password);
        // Ensure customerId is present on user; fallback to local mapping by username
        try {
          if (!user?.customerId) {
            const key = 'userCustomerMap';
            const mapStr = localStorage.getItem(key) || '{}';
            const map = JSON.parse(mapStr);
            const cid = map[this.username];
            if (cid) {
              user = { ...(user || {}), customerId: cid };
            }
          }
        } catch {}
        this.api.setUser(user);
        this.router.navigate(['/customers']);
      },
      error: () => (this.error = 'Login failed')
    });
  }
}
