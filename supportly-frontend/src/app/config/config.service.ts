import {Inject, Injectable, PLATFORM_ID} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {firstValueFrom} from 'rxjs';
import {isPlatformBrowser} from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  private config: any;

  constructor(private http: HttpClient, @Inject(PLATFORM_ID) private platformId: any) {}

  loadConfig(): Promise<void> {
    if (isPlatformBrowser(this.platformId)) {
      return firstValueFrom(this.http.get('/assets/config.json')).then(data => {
        this.config = data; // przypisanie
        // nic nie zwracamy
      });
    } else {
      // podczas builda pipeline SSR / prerendering ustaw fallback
      this.config = { apiUrl: '' };
      return Promise.resolve(); // resolves to void
    }
  }

  get apiUrl(): string {
    return this.config?.apiUrl;
  }
}

