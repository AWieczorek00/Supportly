import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';

export const authenticationGuard: CanActivateFn = () => {
  const router = inject(Router);

  // Upewniamy się, że jesteśmy w środowisku przeglądarki
  if (typeof window === 'undefined') {
    return router.createUrlTree(['/login']);
  }

  const token = localStorage.getItem('token');
  return token ? true : router.createUrlTree(['/login']);
};
