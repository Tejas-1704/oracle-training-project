import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../api.service';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css'
})
export class NotificationsComponent {
  to = '';
  subject = '';
  message = '';

  constructor(private api: ApiService) {}

  send() {
    const payload = {
      eventId: 'evt',
      template: 'simple',
      to: [this.to],
      subject: this.subject,
      model: {},
      metadata: {}
    };
    this.api.sendTestEmail(payload).subscribe(() => (this.message = 'Email queued'));
  }
}
