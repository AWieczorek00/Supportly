import { Injectable } from '@angular/core';
import {BaseApiService} from '../../config/base-api.service';
import {AgreementCriteria} from '../agreement-criteria';
import {Agreement} from '../agreement';

@Injectable({
  providedIn: 'root'
})
export class HttpAgreementService extends BaseApiService<AgreementCriteria,Agreement>{
  protected resourcePath='agreement';



}
