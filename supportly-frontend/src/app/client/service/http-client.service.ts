import { Injectable } from '@angular/core';
import {BaseApiService} from '../../config/base-api.service';
import {AgreementCriteria} from '../../agreement/agreement-criteria';
import {Agreement} from '../../agreement/agreement';
import {ClientCriteria} from '../ClientCriteria';
import {Client} from '../../order/Client';
import {Observable} from 'rxjs';
import {Company} from '../../agreement/company';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService extends BaseApiService<ClientCriteria,Client>{
  protected resourcePath='client';

  searchCompanies(query: string): Observable<Client[]> {
    return this.http.get<Client[]>(this.buildUrl(`/client/list?companyName=${query}`));
  }
}
