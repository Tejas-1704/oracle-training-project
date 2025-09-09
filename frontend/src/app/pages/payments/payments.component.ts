import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../api.service';

@Component({
  selector: 'app-payments',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './payments.component.html',
  styleUrl: './payments.component.css'
})
export class PaymentsComponent implements OnInit {
  payments: any[] = [];
  targetId = '';
  amount: number | null = null;
  method = 'CARD';

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    const params = { targetType: 'POLICY', targetId: this.targetId };
    this.api.getPayments(params).subscribe((data: any) => (this.payments = data));
  }

  create() {
    const payload = { targetType: 'POLICY', targetId: this.targetId, amount: this.amount, method: this.method };
    this.api.createPayment(payload).subscribe(() => {
      this.amount = null;
      this.load();
    });
  }
}
