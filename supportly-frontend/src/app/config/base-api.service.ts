import {ConfigService} from './config.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

export abstract class BaseApiService<TCriteria, TResult> {
  protected abstract resourcePath: string;

  constructor(
    protected http: HttpClient,
    protected configService: ConfigService
  ) {}

  protected buildUrl(path: string): string {
    const baseUrl = this.configService.apiUrl;
    return `${baseUrl}${path.startsWith('/') ? path : '/' + path}`;
  }

  search(criteria: Partial<TCriteria>): Observable<TResult[]> {
    return this.http.post<TResult[]>(
      this.buildUrl(`/${this.resourcePath}/search`),
      criteria
    );
  }

  add(entity: Partial<TResult>): Observable<TResult> {
    return this.http.post<TResult>(
      this.buildUrl(`/${this.resourcePath}`),
      entity
    );
  }
}

