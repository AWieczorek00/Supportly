import { Injectable } from '@angular/core';
import {Order} from './Order';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor() { }

  criteria(): Order[]  {
    return []
  }
}
