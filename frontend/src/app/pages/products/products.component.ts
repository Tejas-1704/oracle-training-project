import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../api.service';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css'
})
export class ProductsComponent implements OnInit {
  products: any[] = [];
  name = '';
  code = '';
  rate: number | null = null;

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.api.getProducts().subscribe((data: any) => (this.products = data));
  }

  create() {
    const payload = { name: this.name, code: this.code, baseRatePer1000: this.rate };
    this.api.createProduct(payload).subscribe(() => {
      this.name = this.code = '';
      this.rate = null;
      this.load();
    });
  }
}
