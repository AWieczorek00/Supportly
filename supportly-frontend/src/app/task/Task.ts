import {Order} from '../order/Order';
import {Employee} from '../employee/Employee';

export interface Task {

  name:String
  employees:Employee[]
  order?:Order
  done:boolean
}
