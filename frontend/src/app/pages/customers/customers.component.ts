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

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.api.getCustomers().subscribe((data: any) => (this.customers = data));
  }

  create() {
    const payload = { firstName: this.firstName, lastName: this.lastName, email: this.email };
    this.api.createCustomer(payload).subscribe(() => {
      this.firstName = this.lastName = this.email = '';
      this.load();
    });
  }
}
