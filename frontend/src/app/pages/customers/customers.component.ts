import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../api.service';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit {
  customers: any[] = [];
  firstName = '';
  lastName = '';
  email = '';
  phone = '';
  dob = '';
  isAdmin = false;

  constructor(private api: ApiService) { }

  ngOnInit() {
    this.isAdmin = this.api.isAdmin();
    this.load();
  }

  load() {
    this.api.getCustomers().subscribe((data: any) => (this.customers = data));
  }

  create() {
    const payload: any = { firstName: this.firstName, lastName: this.lastName, email: this.email };
    if (this.phone) payload.phone = this.phone;
    if (this.dob) payload.dob = this.dob; // YYYY-MM-DD
    this.api.createCustomer(payload).subscribe(() => {
      this.firstName = this.lastName = this.email = '';
      this.phone = this.dob = '';
      this.load();
    });
  }

  enableEdit(c: any) {
    c._editing = true;
    c._edit = {
      firstName: c.firstName,
      lastName: c.lastName,
      email: c.email,
      phone: c.phone || '',
      dob: c.dob || ''
    };
  }

  cancelEdit(c: any) {
    c._editing = false;
    delete c._edit;
  }

  saveEdit(c: any) {
    const updates: any = {};
    const e = c._edit;
    ['firstName', 'lastName', 'email', 'phone', 'dob'].forEach(k => {
      const val = e[k];
      if (k === 'dob' && !val) {
        // Skip empty DOB to avoid sending invalid date string
        return;
      }
      if (val !== undefined && val !== c[k]) updates[k] = val;
    });
    if (Object.keys(updates).length === 0) { c._editing = false; return; }
    this.api.updateCustomer(c.id, updates).subscribe({
      next: (updated) => {
        Object.assign(c, updated);
        c._editing = false;
        delete c._edit;
      },
      error: (err) => {
        const status = err?.status;
        if (status === 403) {
          alert('You do not have permission to update customers (ADMIN only).');
        } else if (status === 400) {
          alert(err?.error?.error || 'Invalid data. Please check your changes.');
        } else {
          alert(err?.error?.error || 'Failed to update customer');
        }
      }
    });
  }



  deleteCustomer(c: any) {
    if (!confirm('Delete this customer?')) return;
    this.api.deleteCustomer(c.id).subscribe({
      next: () => {
        this.customers = this.customers.filter(x => x.id !== c.id);
      },
      error: (err) => alert(err?.error?.error || 'Failed to delete customer')
    });
  }
}
