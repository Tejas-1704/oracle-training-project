import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { CustomersComponent } from './pages/customers/customers.component';
import { ProductsComponent } from './pages/products/products.component';
import { QuotesComponent } from './pages/quotes/quotes.component';
import { PoliciesComponent } from './pages/policies/policies.component';
import { ClaimsComponent } from './pages/claims/claims.component';
import { DocumentsComponent } from './pages/documents/documents.component';
import { PaymentsComponent } from './pages/payments/payments.component';
import { NotificationsComponent } from './pages/notifications/notifications.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'customers', component: CustomersComponent },
  { path: 'products', component: ProductsComponent },
  { path: 'quotes', component: QuotesComponent },
  { path: 'policies', component: PoliciesComponent },
  { path: 'claims', component: ClaimsComponent },
  { path: 'documents', component: DocumentsComponent },
  { path: 'payments', component: PaymentsComponent },
  { path: 'notifications', component: NotificationsComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];
