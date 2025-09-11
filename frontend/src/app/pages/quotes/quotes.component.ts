import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-quotes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './quotes.component.html',
  styleUrl: './quotes.component.css'
})
export class QuotesComponent implements OnInit {
  quotes: any[] = [];
  products: any[] = [];
  isAdmin = false;
  customers: any[] = [];
  customerId = '';
  productId = '';
  sumAssured: number | null = null;
  termMonths: number | null = null;

  constructor(private api: ApiService, private router: Router) {}

  ngOnInit() {
    this.isAdmin = this.api.isAdmin();
    // Load active products for selection
    this.api.getProducts(true).subscribe((data: any) => (this.products = data || []));
    if (this.isAdmin) {
      this.api.getCustomers().subscribe((data: any) => (this.customers = data || []));
    }
    // For users, auto-fill customerId from login payload (customerId was added to response)
    const u = this.api.getUser();
    if (!this.isAdmin && u?.customerId) {
      this.customerId = u.customerId;
    }
    this.load();
  }

  load() {
    this.api.getQuotes().subscribe({
      next: (data: any) => (this.quotes = data),
      error: (err) => {
        // Non-admins are not allowed to list; hide history gracefully
        if (err?.status === 403) this.quotes = [];
      }
    });
  }

  create() {
    if (!this.customerId) { alert('Please select a customer'); return; }
    if (!this.productId) { alert('Please select a product'); return; }
    const payload = {
      customerId: this.customerId,
      productId: this.productId,
      sumAssured: this.sumAssured,
      termMonths: this.termMonths
    };
    this.api.createQuote(payload).subscribe({
      next: (quote: any) => {
        // Clear form
        if (this.isAdmin) this.customerId = '';
        this.productId = '';
        this.sumAssured = this.termMonths = null;
        // Reflect immediately in UI
        if (this.isAdmin) {
          this.load();
        } else {
          // Non-admins can't list; append locally to show creation result
          this.quotes = [quote, ...this.quotes];
        }
      },
      error: (err) => {
        const status = err?.status;
        if (status === 400) alert(err?.error?.error || 'Invalid quote parameters.');
        else if (status === 404) alert('Customer or Product not found.');
        else if (status === 403) alert('You do not have permission to create quotes.');
        else alert(err?.error?.error || 'Failed to create quote');
      }
    });
  }

  price(id: string) {
    this.api.priceQuote(id).subscribe({
      next: () => this.load(),
      error: (err) => {
        const msg = err?.error?.error || 'Failed to price quote';
        alert(msg);
      }
    });
  }

  confirm(id: string) {
    this.api.confirmQuote(id).subscribe({
      next: (policy: any) => {
        alert(`Policy created: ${policy?.policyNumber || policy?.id || 'Success'}`);
        try { localStorage.setItem('lastPolicy', JSON.stringify(policy)); } catch {}
        this.router.navigate(['/policies']);
      },
      error: (err) => {
        const msg = err?.error?.error || 'Failed to accept quote';
        alert(msg);
      }
    });
  }
}
