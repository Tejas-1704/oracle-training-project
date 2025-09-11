import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../api.service';

@Component({
  selector: 'app-policies',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './policies.component.html',
  styleUrl: './policies.component.css'
})
export class PoliciesComponent implements OnInit {
  policies: any[] = [];

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.api.getPolicies().subscribe({
      next: (data: any) => (this.policies = data),
      error: () => {
        const last = localStorage.getItem('lastPolicy');
        if (last) {
          try { this.policies = [JSON.parse(last)]; } catch { this.policies = []; }
        } else {
          this.policies = [];
        }
      }
    });
  }
}
