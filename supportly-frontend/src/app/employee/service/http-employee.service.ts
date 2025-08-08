import { Injectable } from '@angular/core';
import {map, Observable} from 'rxjs';
import {EmployeeCriteria} from '../EmployeeCriteria';
import {Employee} from '../Employee';
import {BaseApiService} from '../../config/base-api.service';

@Injectable({
  providedIn: 'root'
})
export class HttpEmployeeService extends BaseApiService<EmployeeCriteria, Employee>{

  protected resourcePath = 'employee';


}
