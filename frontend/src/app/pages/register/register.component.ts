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
  role = 'USER';
  message = '';
  error = '';

  constructor(private api: ApiService, private router: Router) {}

  register() {
    this.api.register({ username: this.username, password: this.password, role: this.role }).subscribe({
      next: () => {
        this.message = 'Registration successful';
        this.error = '';
        setTimeout(() => this.router.navigate(['/login']), 1000);
      },
      error: () => {
        this.error = 'Registration failed';
        this.message = '';
      }
    });
  }
}
