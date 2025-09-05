import svc from '../config/serviceConfig.js';

const authHeader = (user, pw) => ({
  'Authorization': 'Basic ' + btoa(`${user}:${pw}`),
  'Content-Type': 'application/json'
});

export async function register(data) {
  const res = await fetch(`${svc.core}/auth/register`, {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(data)
  });
  return res.json();
}

export async function login(data) {
  const res = await fetch(`${svc.core}/auth/login`, {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(data)
  });
  return res.json();
}

export async function getCustomers(auth, query = '') {
  const url = `${svc.core}/customers${query ? `?q=${encodeURIComponent(query)}` : ''}`;
  const res = await fetch(url, { headers: authHeader(auth.user, auth.pw) });
  return res.json();
}

// Customer routes
export async function createCustomer(auth, data) {
  const res = await fetch(`${svc.core}/customers`, {
    method: 'POST',
    headers: authHeader(auth.user, auth.pw),
    body: JSON.stringify(data)
  });
  return res.json();
}

export async function getCustomer(auth, id) {
  const res = await fetch(`${svc.core}/customers/${id}`, {
    headers: authHeader(auth.user, auth.pw)
  });
  return res.json();
}

export async function updateCustomer(auth, id, data) {
  const res = await fetch(`${svc.core}/customers/${id}`, {
    method: 'PATCH',
    headers: authHeader(auth.user, auth.pw),
    body: JSON.stringify(data)
  });
  return res.json();
}

export async function deleteCustomer(auth, id) {
  const res = await fetch(`${svc.core}/customers/${id}`, {
    method: 'DELETE',
    headers: authHeader(auth.user, auth.pw)
  });
  return res.ok;
}

// Product routes
export async function createProduct(auth, data) {
  const res = await fetch(`${svc.core}/products`, {
    method: 'POST',
    headers: authHeader(auth.user, auth.pw),
    body: JSON.stringify(data)
  });
  return res.json();
}

export async function getProducts(auth, query = '') {
  const url = `${svc.core}/products${query ? `?${query}` : ''}`;
  const res = await fetch(url, { headers: authHeader(auth.user, auth.pw) });
  return res.json();
}

export async function getProduct(auth, id) {
  const res = await fetch(`${svc.core}/products/${id}`, {
    headers: authHeader(auth.user, auth.pw)
  });
  return res.json();
}

export async function updateProduct(auth, id, data) {
  const res = await fetch(`${svc.core}/products/${id}`, {
    method: 'PATCH',
    headers: authHeader(auth.user, auth.pw),
    body: JSON.stringify(data)
  });
  return res.json();
}

export async function deleteProduct(auth, id) {
  const res = await fetch(`${svc.core}/products/${id}`, {
    method: 'DELETE',
    headers: authHeader(auth.user, auth.pw)
  });
  return res.ok;
}

// Quote routes
export async function createQuote(auth, data) {
  const res = await fetch(`${svc.core}/quotes`, {
    method: 'POST',
    headers: authHeader(auth.user, auth.pw),
    body: JSON.stringify(data)
  });
  return res.json();
}

export async function getQuotes(auth, query = '') {
  const url = `${svc.core}/quotes${query ? `?${query}` : ''}`;
  const res = await fetch(url, { headers: authHeader(auth.user, auth.pw) });
  return res.json();
}

export async function getQuote(auth, id) {
  const res = await fetch(`${svc.core}/quotes/${id}`, {
    headers: authHeader(auth.user, auth.pw)
  });
  return res.json();
}

export async function updateQuote(auth, id, data) {
  const res = await fetch(`${svc.core}/quotes/${id}`, {
    method: 'PATCH',
    headers: authHeader(auth.user, auth.pw),
    body: JSON.stringify(data)
  });
  return res.json();
}

export async function priceQuote(auth, id) {
  const res = await fetch(`${svc.core}/quotes/${id}/price`, {
    method: 'POST',
    headers: authHeader(auth.user, auth.pw)
  });
  return res.json();
}

export async function confirmQuote(auth, id) {
  const res = await fetch(`${svc.core}/quotes/${id}/confirm`, {
    method: 'POST',
    headers: authHeader(auth.user, auth.pw)
  });
  return res.json();
}

// Policy routes
export async function getPolicies(auth, query = '') {
  const url = `${svc.core}/policies${query ? `?${query}` : ''}`;
  const res = await fetch(url, { headers: authHeader(auth.user, auth.pw) });
  return res.json();
}

export async function getPolicy(auth, id) {
  const res = await fetch(`${svc.core}/policies/${id}`, {
    headers: authHeader(auth.user, auth.pw)
  });
  return res.json();
}

// Claim routes
export async function createClaim(auth, data) {
  const res = await fetch(`${svc.core}/claims`, {
    method: 'POST',
    headers: authHeader(auth.user, auth.pw),
    body: JSON.stringify(data)
  });
  return res.json();
}

export async function getClaims(auth, query = '') {
  const url = `${svc.core}/claims${query ? `?${query}` : ''}`;
  const res = await fetch(url, { headers: authHeader(auth.user, auth.pw) });
  return res.json();
}

export async function getClaim(auth, id) {
  const res = await fetch(`${svc.core}/claims/${id}`, {
    headers: authHeader(auth.user, auth.pw)
  });
  return res.json();
}

export async function updateClaim(auth, id, data) {
  const res = await fetch(`${svc.core}/claims/${id}`, {
    method: 'PATCH',
    headers: authHeader(auth.user, auth.pw),
    body: JSON.stringify(data)
  });
  return res.json();
}

export async function assessClaim(auth, id, data) {
  const res = await fetch(`${svc.core}/claims/${id}/assess`, {
    method: 'POST',
    headers: authHeader(auth.user, auth.pw),
    body: JSON.stringify(data)
  });
  return res.json();
}

export async function closeClaim(auth, id) {
  const res = await fetch(`${svc.core}/claims/${id}/close`, {
    method: 'POST',
    headers: authHeader(auth.user, auth.pw)
  });
  return res.json();
}

// Document service routes (no auth)
export async function uploadDocument(meta, file) {
  const form = new FormData();
  form.append('meta', new Blob([JSON.stringify(meta)], { type: 'application/json' }));
  form.append('file', file);
  const res = await fetch(`${svc.document}/documents`, {
    method: 'POST',
    body: form
  });
  return res.json();
}

export async function listDocuments(params) {
  const q = new URLSearchParams(params).toString();
  const res = await fetch(`${svc.document}/documents${q ? `?${q}` : ''}`);
  return res.json();
}

export async function getDocument(id) {
  const res = await fetch(`${svc.document}/documents/${id}`);
  return res.json();
}

export async function deleteDocument(id) {
  const res = await fetch(`${svc.document}/documents/${id}`, {
    method: 'DELETE'
  });
  return res.ok;
}

// Payment service routes (no auth)
export async function createPayment(data) {
  const res = await fetch(`${svc.payment}/payments`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  });
  return res.json();
}

export async function getPayments(params) {
  const q = new URLSearchParams(params).toString();
  const res = await fetch(`${svc.payment}/payments${q ? `?${q}` : ''}`);
  return res.json();
}

export async function getPayment(id) {
  const res = await fetch(`${svc.payment}/payments/${id}`);
  return res.json();
}

// Notification service routes (no auth)
export async function sendTestEmail(data) {
  const res = await fetch(`${svc.notification}/emails/test`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  });
  return res.json();
}

export async function emailHealth() {
  const res = await fetch(`${svc.notification}/emails/health`);
  return res.json();
}
