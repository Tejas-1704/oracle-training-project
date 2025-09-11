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
  isAdmin = false;
  uploadFiles: { [id: string]: File | null } = {};

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.isAdmin = this.api.isAdmin();
    this.load();
  }

  load() {
    this.api.getClaims().subscribe({
      next: (data: any) => (this.claims = data),
      error: (err) => { if (err?.status === 403) this.claims = []; }
    });
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

  onFileChange(evt: any, claimId: string) {
    const file = evt?.target?.files?.[0] || null;
    this.uploadFiles[claimId] = file;
  }

  uploadDoc(claim: any) {
    const file = this.uploadFiles[claim.id];
    if (!file) { alert('Please choose a file first'); return; }
    const meta = { ownerId: claim.id, ownerType: 'CLAIM', tags: ['evidence'] };
    this.api.uploadDocument(meta, file).subscribe({
      next: () => { alert('Document uploaded'); this.uploadFiles[claim.id] = null; },
      error: () => alert('Failed to upload document')
    });
  }
}
