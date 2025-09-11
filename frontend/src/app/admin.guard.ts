import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { ApiService } from './api.service';

export const adminGuard: CanActivateFn = () => {
  const api = inject(ApiService);
  const router = inject(Router);
  if (api.isAuthenticated() && api.isAdmin()) {
    return true;
  }
  // Redirect non-admins to a safe page
  router.navigate(['/products']);
  return false;
};

