import {Injectable} from '@angular/core';
import {BaseApiService} from '../../config/base-api.service';
import {OrderCriteria} from '../OrderCriteria';
import {Order} from '../Order';

@Injectable({
  providedIn: 'root'
})
export class HttpOrderService extends BaseApiService<OrderCriteria, Order> {

  protected resourcePath = 'order';


}
