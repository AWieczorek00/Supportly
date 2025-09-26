import {Inject, Injectable, PLATFORM_ID} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {isPlatformBrowser} from '@angular/common';
import {ConfigService} from '../config/config.service';

@Injectable({
  providedIn: 'root'
})
export class RequestInterceptor implements HttpInterceptor {

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private configService: ConfigService
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let token: string | null = null;

    if (isPlatformBrowser(this.platformId)) {
      token = localStorage.getItem('token');
    }

    // używamy apiUrl z ConfigService
    const apiUrl = this.configService.apiUrl || '';

    // jeśli request nie jest absolutny, dopisujemy base URL
    const url = req.url.startsWith('http') ? req.url : apiUrl + req.url;

    const cloned = req.clone({
      url,
      setHeaders: token ? { Authorization: `Bearer ${token}` } : {}
    });

    return next.handle(cloned);
  }
}

