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
  description = '';
  minSumAssured: number | null = null;
  maxSumAssured: number | null = null;
  minTermMonths: number | null = null;
  maxTermMonths: number | null = null;
  activeSelect: 'true' | 'false' = 'true';
  isAdmin = false;
  showActive = true;

  constructor(private api: ApiService) { }

  ngOnInit() {
    this.isAdmin = this.api.isAdmin();
    this.load();
  }

  load() {
    const activeParam = this.isAdmin ? this.showActive : true; // non-admins see active products
    this.api.getProducts(activeParam).subscribe((data: any) => (this.products = data));
  }

  toggleActive()
  {
    // Called when admin flips the toggle
    this.load();
  }

  create() {
    const payload: any = {
      name: this.name,
      code: this.code,
      description: this.description,
      baseRatePer1000: this.coerceNumber(this.rate),
      minSumAssured: this.coerceNumber(this.minSumAssured),
      maxSumAssured: this.coerceNumber(this.maxSumAssured),
      minTermMonths: this.coerceNumber(this.minTermMonths),
      maxTermMonths: this.coerceNumber(this.maxTermMonths),
      active: this.activeSelect === 'true'
    };
    this.api.createProduct(payload).subscribe({
      next: () => {
        this.name = this.code = '';
        this.description = '';
        this.rate = this.minSumAssured = this.maxSumAssured = null;
        this.minTermMonths = this.maxTermMonths = null;
        this.activeSelect = 'true';
        this.load();
      },
      error: (err) => {
        const status = err?.status;
        if (status === 400) alert(err?.error?.error || 'Invalid product data.');
        else if (status === 403) alert('You do not have permission to create products (ADMIN only).');
        else alert(err?.error?.error || 'Failed to create product');
      }
    });
  }

  enableEdit(p: any) {
    if (!this.isAdmin) return;
    p._editing = true;
    p._edit = {
      name: p.name,
      code: p.code,
      description: p.description || '',
      baseRatePer1000: p.baseRatePer1000,
      minSumAssured: p.minSumAssured,
      maxSumAssured: p.maxSumAssured,
      minTermMonths: p.minTermMonths,
      maxTermMonths: p.maxTermMonths,
      active: !!p.active
    };
  }

  cancelEdit(p: any) {
    p._editing = false;
    delete p._edit;
  }

  private coerceNumber(val: any): number | undefined {
    if (val === null || val === undefined || val === '') return undefined;
    const n = Number(val);
    return isNaN(n) ? undefined : n;
  }

  saveEdit(p: any) {
    const e = p._edit || {};
    const updates: any = {};
    const fields = ['name','code','description'];
    fields.forEach(k => {
      if (e[k] !== undefined && e[k] !== p[k]) updates[k] = e[k];
    });
    const numFields = ['baseRatePer1000','minSumAssured','maxSumAssured','minTermMonths','maxTermMonths'];
    numFields.forEach(k => {
      const coerced = this.coerceNumber(e[k]);
      if (coerced !== undefined && coerced !== p[k]) updates[k] = coerced;
    });
    if (typeof e.active === 'boolean' && e.active !== !!p.active) updates['active'] = e.active;

    if (Object.keys(updates).length === 0) { p._editing = false; return; }
    this.api.updateProduct(p.id, updates).subscribe({
      next: (updated) => {
        Object.assign(p, updated);
        p._editing = false;
        delete p._edit;
      },
      error: (err) => {
        const status = err?.status;
        if (status === 403) alert('You do not have permission to update products (ADMIN only).');
        else if (status === 400) alert(err?.error?.error || 'Invalid product data.');
        else alert(err?.error?.error || 'Failed to update product');
      }
    });
  }

  deleteProduct(p: any) {
    if (!this.isAdmin) return;
    if (!confirm('Delete this product?')) return;
    this.api.deleteProduct(p.id).subscribe({
      next: () => { this.products = this.products.filter(x => x.id !== p.id); },
      error: (err) => alert(err?.error?.error || 'Failed to delete product')
    });
  }
}
