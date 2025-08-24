import {Injectable} from '@angular/core';
import {BaseApiService} from '../../config/base-api.service';
import {OrderCriteria} from '../OrderCriteria';
import {Order} from '../Order';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HttpOrderService extends BaseApiService<OrderCriteria, Order> {

  protected resourcePath = 'order';

  searchOrders(query: string): Observable<Order[]> {
    return this.http.get<Order[]>(this.buildUrl(`/${this.resourcePath}/list?companyName=${query}`));
  }
}
