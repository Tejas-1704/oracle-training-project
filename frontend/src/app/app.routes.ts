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
import { authGuard } from './auth.guard';
import { adminGuard } from './admin.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'customers', component: CustomersComponent, canActivate: [authGuard, adminGuard] },
  { path: 'products', component: ProductsComponent, canActivate: [authGuard] },
  { path: 'quotes', component: QuotesComponent, canActivate: [authGuard] },
  { path: 'policies', component: PoliciesComponent, canActivate: [authGuard] },
  { path: 'claims', component: ClaimsComponent, canActivate: [authGuard] },
  { path: 'documents', component: DocumentsComponent, canActivate: [authGuard] },
  { path: 'payments', component: PaymentsComponent, canActivate: [authGuard] },
  { path: 'notifications', component: NotificationsComponent, canActivate: [authGuard] },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];
