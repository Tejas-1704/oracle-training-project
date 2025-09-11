import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../api.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  username = '';
  password = '';
  confirmPassword = '';
  role = 'USER';
  // Customer fields
  firstName = '';
  lastName = '';
  email = '';
  phone = '';
  dob = '';
  message = '';
  error = '';

  constructor(private api: ApiService, private router: Router) {}

  register() {
    this.error = this.message = '';
    if (!this.password || this.password !== this.confirmPassword) {
      this.error = 'Passwords do not match';
      return;
    }
    // Basic client-side required checks for personal info
    if (!this.username || !this.firstName || !this.lastName || !this.email) {
      this.error = 'Please fill all required fields';
      return;
    }

    const customer: any = {
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email
    };
    if (this.phone) customer.phone = this.phone;
    if (this.dob) customer.dob = this.dob; // YYYY-MM-DD

    const user = { username: this.username, password: this.password, email: this.email, role: 'USER' };

    // Try single-call registration that links customer; if not, fall back to two-step
    this.api.clearAuth();
    this.api.publicRegister(customer, user).subscribe({
      next: (res: any) => {
        if (res?.customerId) {
          // Linked successfully in one shot
          this.message = 'Registration successful';
          this.error = '';
          setTimeout(() => this.router.navigate(['/login']), 1000);
          return;
        }
        // Fallback: register user then create customer with USER role now allowed
        this.api.register({ username: user.username, password: user.password, email: this.email, role: 'USER' }).subscribe({
          next: () => {
            this.api.setAuth(this.username, this.password);
            this.api.createCustomer(customer).subscribe({
              next: () => {
                this.message = 'Registration successful';
                this.error = '';
                setTimeout(() => this.router.navigate(['/login']), 1000);
              },
              error: (custErr) => {
                const msg = custErr?.error?.error || custErr?.message || 'Failed to create customer profile';
                this.error = msg;
                this.message = '';
              }
            });
          },
          error: (regErr) => {
            const msg = regErr?.error?.error || regErr?.message || 'Failed to create app user';
            this.error = msg;
            this.message = '';
          }
        });
      },
      error: (err) => {
        const msg = err?.error?.error || err?.message || 'Registration failed';
        this.error = msg;
        this.message = '';
      }
    });
  }
}
