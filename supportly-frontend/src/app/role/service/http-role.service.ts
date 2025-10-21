import { Injectable } from '@angular/core';
import {BaseApiService} from '../../config/base-api.service';
import {OrderCriteria} from '../../order/OrderCriteria';
import {Order} from '../../order/Order';
import {Observable} from 'rxjs';
import {Role} from '../Role';

@Injectable({
  providedIn: 'root'
})
export class HttpRoleService extends BaseApiService<OrderCriteria, Order> {

  protected resourcePath = 'role';

  allRole(): Observable<Role[]> {
    return this.http.get<Role[]>(this.buildUrl(`/${this.resourcePath}/all`));
  }
}
