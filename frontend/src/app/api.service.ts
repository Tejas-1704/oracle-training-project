import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { serviceConfig } from './service-config';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private authHeader: string | null = null;

  constructor(private http: HttpClient) {}

  setAuth(username: string, password: string) {
    this.authHeader = btoa(`${username}:${password}`);
  }

  private authOptions(options: any = {}) {
    if (this.authHeader) {
      options.headers = new HttpHeaders({ Authorization: `Basic ${this.authHeader}` });
    }
    return options;
  }

  // Auth
  register(data: any) {
    return this.http.post(`${serviceConfig.core}/auth/register`, data);
  }
  login(data: any) {
    return this.http.post(`${serviceConfig.core}/auth/login`, data);
  }

  // Customers
  createCustomer(data: any) {
    return this.http.post(`${serviceConfig.core}/customers`, data, this.authOptions());
  }
  getCustomers(query?: string) {
    const params = query ? new HttpParams().set('q', query) : undefined;
    return this.http.get(`${serviceConfig.core}/customers`, this.authOptions({ params }));
  }
  getCustomer(id: string) {
    return this.http.get(`${serviceConfig.core}/customers/${id}`, this.authOptions());
  }
  updateCustomer(id: string, data: any) {
    return this.http.patch(`${serviceConfig.core}/customers/${id}`, data, this.authOptions());
  }
  deleteCustomer(id: string) {
    return this.http.delete(`${serviceConfig.core}/customers/${id}`, this.authOptions());
  }

  // Products
  createProduct(data: any) {
    return this.http.post(`${serviceConfig.core}/products`, data, this.authOptions());
  }
  getProducts(active?: boolean) {
    const params = active !== undefined ? new HttpParams().set('active', active) : undefined;
    return this.http.get(`${serviceConfig.core}/products`, this.authOptions({ params }));
  }
  getProduct(id: string) {
    return this.http.get(`${serviceConfig.core}/products/${id}`, this.authOptions());
  }
  updateProduct(id: string, data: any) {
    return this.http.patch(`${serviceConfig.core}/products/${id}`, data, this.authOptions());
  }
  deleteProduct(id: string) {
    return this.http.delete(`${serviceConfig.core}/products/${id}`, this.authOptions());
  }

  // Quotes
  createQuote(data: any) {
    return this.http.post(`${serviceConfig.core}/quotes`, data, this.authOptions());
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
    return this.http.post(`${serviceConfig.core}/quotes/${id}/price`, {}, this.authOptions());
  }
  confirmQuote(id: string) {
    return this.http.post(`${serviceConfig.core}/quotes/${id}/confirm`, {}, this.authOptions());
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
