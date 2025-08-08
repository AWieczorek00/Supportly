import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RequestInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Pobierz token z localStorage
    const token = localStorage.getItem('token');
    console.log('Interceptor called. Token:', token);

    if (token) {

      // Klonuj żądanie i dodaj nagłówek Authorization
      const cloned = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      return next.handle(cloned);
    } else {
      // Jeśli brak tokena, przekazuj żądanie bez zmian
      return next.handle(req);
    }
  }
}

