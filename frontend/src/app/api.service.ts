import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { serviceConfig } from './service-config';
import { switchMap, catchError, of, throwError } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private authHeader: string | null = null;
  private roles: string[] = [];
  private user: any | null = null;

  constructor(private http: HttpClient) {
    // Load auth header from storage for persistence across reloads
    const stored = localStorage.getItem('authHeader');
    if (stored) {
      this.authHeader = stored;
    }
    const rolesStr = localStorage.getItem('roles');
    if (rolesStr) {
      try { this.roles = JSON.parse(rolesStr) || []; } catch { this.roles = []; }
    }
    const userStr = localStorage.getItem('user');
    if (userStr) {
      try { this.user = JSON.parse(userStr); } catch { this.user = null; }
    }
  }

  setAuth(username: string, password: string) {
    this.authHeader = btoa(`${username}:${password}`);
    localStorage.setItem('authHeader', this.authHeader);
  }

  clearAuth() {
    this.authHeader = null;
    localStorage.removeItem('authHeader');
    this.roles = [];
    localStorage.removeItem('roles');
    this.user = null;
    localStorage.removeItem('user');
  }

  isAuthenticated(): boolean {
    return !!this.authHeader;
  }

  setUser(user: any) {
    const roles = Array.isArray(user?.roles) ? user.roles : [];
    this.roles = roles;
    localStorage.setItem('roles', JSON.stringify(this.roles));
    this.user = user || null;
    localStorage.setItem('user', JSON.stringify(this.user));
  }

  getRoles(): string[] { return this.roles; }
  isAdmin(): boolean { return this.roles.includes('ADMIN'); }
  getUser(): any | null { return this.user; }

  private authOptions(options: any = {}) {
    if (this.authHeader) {
      options.headers = new HttpHeaders({ Authorization: `Basic ${this.authHeader}` });
    }
    return options;
  }

  private authJsonOptions(options: any = {}) {
    const base: Record<string, string> = { 'Content-Type': 'application/json' };
    if (this.authHeader) {
      base['Authorization'] = `Basic ${this.authHeader}`;
    }
    options.headers = new HttpHeaders(base);
    return options;
  }

  // Auth
  register(data: any) {
    return this.http.post(`${serviceConfig.core}/auth/register`, data);
  }
  /**
   * Two-step registration helper:
   * 1) Creates a customer with personal details
   * 2) Registers an app user linked to that customer (via customerId)
   */
  registerCustomerAndUser(customer: {
    firstName: string;
    lastName: string;
    email: string;
    phone?: string;
    dob?: string; // YYYY-MM-DD
  }, user: { username: string; password: string; email?: string; role?: string; }) {
    return this.http
      .post(`${serviceConfig.core}/customers`, customer, this.authJsonOptions())
      .pipe(
        switchMap((created: any) => {
          const payload = {
            username: user.username,
            password: user.password,
            role: user.role || 'USER',
            email: user.email || customer.email,
            customerId: created?.id
          };
          return this.http.post(`${serviceConfig.core}/auth/register`, payload);
        })
      );
  }

  /**
   * Public registration helper that does not require auth.
   * Tries nested customer payload first; falls back to flattened fields if server rejects it.
   */
  publicRegister(customer: {
    firstName: string;
    lastName: string;
    email: string;
    phone?: string;
    dob?: string;
  }, user: { username: string; password: string; email?: string; role?: string; }) {
    const role = user.role || 'USER';
    const flattened: any = {
      username: user.username,
      password: user.password,
      role,
      firstName: customer.firstName,
      lastName: customer.lastName,
      email: user.email || customer.email
    };
    if (customer.phone) flattened.phone = customer.phone;
    if (customer.dob) flattened.dob = customer.dob;

    // Send flattened by default for robust server mapping
    return this.http.post(`${serviceConfig.core}/auth/register`, flattened);
  }
  login(data: any) {
    return this.http.post(`${serviceConfig.core}/auth/login`, data);
  }

  // Customers
  createCustomer(data: any) {
    return this.http.post(`${serviceConfig.core}/customers`, data, this.authJsonOptions());
  }
  // Public customer creation without Authorization header (for registration flow)
  createCustomerPublic(data: any) {
    return this.http.post(
      `${serviceConfig.core}/customers`,
      data,
      { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }
    );
  }
  getCustomers(query?: string) {
    const params = query ? new HttpParams().set('q', query) : undefined;
    return this.http.get(`${serviceConfig.core}/customers`, this.authOptions({ params }));
  }
  getCustomer(id: string) {
    return this.http.get(`${serviceConfig.core}/customers/${id}`, this.authOptions());
  }
  updateCustomer(id: string, data: any) {
    return this.http.patch(`${serviceConfig.core}/customers/${id}`, data, this.authJsonOptions());
  }

  deleteCustomer(id: string) {
    return this.http.delete(`${serviceConfig.core}/customers/${id}`, this.authOptions());
  }

  // Products
  createProduct(data: any) {
    return this.http.post(`${serviceConfig.core}/products`, data, this.authJsonOptions());
  }
  getProducts(active?: boolean) {
    const params = active !== undefined ? new HttpParams().set('active', active) : undefined;
    return this.http.get(`${serviceConfig.core}/products`, this.authOptions({ params }));
  }
  getProduct(id: string) {
    return this.http.get(`${serviceConfig.core}/products/${id}`, this.authOptions());
  }
  updateProduct(id: string, data: any) {
    return this.http.patch(`${serviceConfig.core}/products/${id}`, data, this.authJsonOptions());
  }
  deleteProduct(id: string) {
    return this.http.delete(`${serviceConfig.core}/products/${id}`, this.authOptions());
  }

  // Quotes
  createQuote(data: any) {
    return this.http.post(`${serviceConfig.core}/quotes`, data, this.authJsonOptions());
  }
  getQuotes(params?: any) {
    return this.http.get(`${serviceConfig.core}/quotes`, this.authOptions({ params }));
  }
  getQuote(id: string) {
    return this.http.get(`${serviceConfig.core}/quotes/${id}`, this.authOptions());
  }
  updateQuote(id: string, data: any) {
    return this.http.patch(`${serviceConfig.core}/quotes/${id}`, data, this.authOptions());
  }
  priceQuote(id: string) {
    return this.http.post(`${serviceConfig.core}/quotes/${id}/price`, {}, this.authJsonOptions());
  }
  confirmQuote(id: string) {
    return this.http.post(`${serviceConfig.core}/quotes/${id}/confirm`, {}, this.authJsonOptions());
  }

  // Policies
  getPolicies(params?: any) {
    return this.http.get(`${serviceConfig.core}/policies`, this.authOptions({ params }));
  }
  getPolicy(id: string) {
    return this.http.get(`${serviceConfig.core}/policies/${id}`, this.authOptions());
  }

  // Claims
  createClaim(data: any) {
    return this.http.post(`${serviceConfig.core}/claims`, data, this.authOptions());
  }
  getClaims(params?: any) {
    return this.http.get(`${serviceConfig.core}/claims`, this.authOptions({ params }));
  }
  getClaim(id: string) {
    return this.http.get(`${serviceConfig.core}/claims/${id}`, this.authOptions());
  }
  updateClaim(id: string, data: any) {
    return this.http.patch(`${serviceConfig.core}/claims/${id}`, data, this.authOptions());
  }
  assessClaim(id: string, data: any) {
    return this.http.post(`${serviceConfig.core}/claims/${id}/assess`, data, this.authOptions());
  }
  closeClaim(id: string) {
    return this.http.post(`${serviceConfig.core}/claims/${id}/close`, {}, this.authOptions());
  }

  // Documents
  uploadDocument(meta: any, file: File) {
    const formData = new FormData();
    formData.append('meta', new Blob([JSON.stringify(meta)], { type: 'application/json' }));
    formData.append('file', file);
    return this.http.post(`${serviceConfig.document}/documents`, formData, this.authOptions());
  }
  getDocuments(params: any) {
    return this.http.get(`${serviceConfig.document}/documents`, this.authOptions({ params }));
  }
  deleteDocument(id: string) {
    return this.http.delete(`${serviceConfig.document}/documents/${id}`, this.authOptions());
  }

  // Payments
  createPayment(data: any) {
    return this.http.post(`${serviceConfig.payment}/payments`, data, this.authOptions());
  }
  getPayments(params: any) {
    return this.http.get(`${serviceConfig.payment}/payments`, this.authOptions({ params }));
  }
  getPayment(id: string) {
    return this.http.get(`${serviceConfig.payment}/payments/${id}`, this.authOptions());
  }

  // Notifications
  sendTestEmail(data: any) {
    return this.http.post(`${serviceConfig.notification}/emails/test`, data, this.authOptions());
  }
  getEmailHealth() {
    return this.http.get(`${serviceConfig.notification}/emails/health`, this.authOptions());
  }
}
