import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../api.service';

@Component({
  selector: 'app-documents',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './documents.component.html',
  styleUrl: './documents.component.css'
})
export class DocumentsComponent {
  documents: any[] = [];
  ownerId = '';
  ownerType = '';
  file?: File;

  constructor(private api: ApiService) {}

  select(event: any) {
    this.file = event.target.files[0];
  }

  upload() {
    if (!this.file) return;
    const meta = { ownerId: this.ownerId, ownerType: this.ownerType, label: this.file.name };
    this.api.uploadDocument(meta, this.file).subscribe(() => {
      this.ownerId = this.ownerType = '';
      this.file = undefined;
      this.list();
    });
  }

  list() {
    const params = { ownerId: this.ownerId, ownerType: this.ownerType };
    this.api.getDocuments(params).subscribe((data: any) => (this.documents = data));
  }
}
