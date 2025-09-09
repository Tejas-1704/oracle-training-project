import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { serviceConfig } from './service-config';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) {}

  // Auth
  register(data: any) {
    return this.http.post(`${serviceConfig.core}/auth/register`, data);
  }
  login(data: any) {
    return this.http.post(`${serviceConfig.core}/auth/login`, data);
  }

  // Customers
  createCustomer(data: any) {
    return this.http.post(`${serviceConfig.core}/customers`, data);
  }
  getCustomers(query?: string) {
    const params = query ? new HttpParams().set('q', query) : undefined;
    return this.http.get(`${serviceConfig.core}/customers`, { params });
  }
  getCustomer(id: string) {
    return this.http.get(`${serviceConfig.core}/customers/${id}`);
  }
  updateCustomer(id: string, data: any) {
    return this.http.patch(`${serviceConfig.core}/customers/${id}`, data);
  }
  deleteCustomer(id: string) {
    return this.http.delete(`${serviceConfig.core}/customers/${id}`);
  }

  // Products
  createProduct(data: any) {
    return this.http.post(`${serviceConfig.core}/products`, data);
  }
  getProducts(active?: boolean) {
    const params = active !== undefined ? new HttpParams().set('active', active) : undefined;
    return this.http.get(`${serviceConfig.core}/products`, { params });
  }
  getProduct(id: string) {
    return this.http.get(`${serviceConfig.core}/products/${id}`);
  }
  updateProduct(id: string, data: any) {
    return this.http.patch(`${serviceConfig.core}/products/${id}`, data);
  }
  deleteProduct(id: string) {
    return this.http.delete(`${serviceConfig.core}/products/${id}`);
  }

  // Quotes
  createQuote(data: any) {
    return this.http.post(`${serviceConfig.core}/quotes`, data);
  }
  getQuotes(params?: any) {
    return this.http.get(`${serviceConfig.core}/quotes`, { params });
  }
  getQuote(id: string) {
    return this.http.get(`${serviceConfig.core}/quotes/${id}`);
  }
  updateQuote(id: string, data: any) {
    return this.http.patch(`${serviceConfig.core}/quotes/${id}`, data);
  }
  priceQuote(id: string) {
    return this.http.post(`${serviceConfig.core}/quotes/${id}/price`, {});
  }
  confirmQuote(id: string) {
    return this.http.post(`${serviceConfig.core}/quotes/${id}/confirm`, {});
  }

  // Policies
  getPolicies(params?: any) {
    return this.http.get(`${serviceConfig.core}/policies`, { params });
  }
  getPolicy(id: string) {
    return this.http.get(`${serviceConfig.core}/policies/${id}`);
  }

  // Claims
  createClaim(data: any) {
    return this.http.post(`${serviceConfig.core}/claims`, data);
  }
  getClaims(params?: any) {
    return this.http.get(`${serviceConfig.core}/claims`, { params });
  }
  getClaim(id: string) {
    return this.http.get(`${serviceConfig.core}/claims/${id}`);
  }
  updateClaim(id: string, data: any) {
    return this.http.patch(`${serviceConfig.core}/claims/${id}`, data);
  }
  assessClaim(id: string, data: any) {
    return this.http.post(`${serviceConfig.core}/claims/${id}/assess`, data);
  }
  closeClaim(id: string) {
    return this.http.post(`${serviceConfig.core}/claims/${id}/close`, {});
  }

  // Documents
  uploadDocument(meta: any, file: File) {
    const formData = new FormData();
    formData.append('meta', new Blob([JSON.stringify(meta)], { type: 'application/json' }));
    formData.append('file', file);
    return this.http.post(`${serviceConfig.document}/documents`, formData);
  }
  getDocuments(params: any) {
    return this.http.get(`${serviceConfig.document}/documents`, { params });
  }
  deleteDocument(id: string) {
    return this.http.delete(`${serviceConfig.document}/documents/${id}`);
  }

  // Payments
  createPayment(data: any) {
    return this.http.post(`${serviceConfig.payment}/payments`, data);
  }
  getPayments(params: any) {
    return this.http.get(`${serviceConfig.payment}/payments`, { params });
  }
  getPayment(id: string) {
    return this.http.get(`${serviceConfig.payment}/payments/${id}`);
  }

  // Notifications
  sendTestEmail(data: any) {
    return this.http.post(`${serviceConfig.notification}/emails/test`, data);
  }
  getEmailHealth() {
    return this.http.get(`${serviceConfig.notification}/emails/health`);
  }
}
