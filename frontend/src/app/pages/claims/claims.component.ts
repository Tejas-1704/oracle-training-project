import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../api.service';

@Component({
  selector: 'app-claims',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './claims.component.html',
  styleUrl: './claims.component.css'
})
export class ClaimsComponent implements OnInit {
  claims: any[] = [];
  policyId = '';
  lossDate = '';
  description = '';

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.api.getClaims().subscribe((data: any) => (this.claims = data));
  }

  create() {
    const payload = { policyId: this.policyId, lossDate: this.lossDate, description: this.description };
    this.api.createClaim(payload).subscribe(() => {
      this.policyId = this.lossDate = this.description = '';
      this.load();
    });
  }

  assess(id: string) {
    const data = { decision: 'APPROVE', approvedAmount: 0, reason: 'Auto' };
    this.api.assessClaim(id, data).subscribe(() => this.load());
  }

  close(id: string) {
    this.api.closeClaim(id).subscribe(() => this.load());
  }
}
