import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../api.service';

@Component({
  selector: 'app-quotes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './quotes.component.html',
  styleUrl: './quotes.component.css'
})
export class QuotesComponent implements OnInit {
  quotes: any[] = [];
  customerId = '';
  productId = '';
  sumAssured: number | null = null;
  termMonths: number | null = null;

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.api.getQuotes().subscribe((data: any) => (this.quotes = data));
  }

  create() {
    const payload = {
      customerId: this.customerId,
      productId: this.productId,
      sumAssured: this.sumAssured,
      termMonths: this.termMonths
    };
    this.api.createQuote(payload).subscribe(() => {
      this.customerId = this.productId = '';
      this.sumAssured = this.termMonths = null;
      this.load();
    });
  }

  price(id: string) {
    this.api.priceQuote(id).subscribe(() => this.load());
  }

  confirm(id: string) {
    this.api.confirmQuote(id).subscribe(() => this.load());
  }
}
