import {Order} from '../order/Order';
import {Employee} from '../employee/Employee';

export interface Task {

  id:number|null
  name:String
  employees:Employee[]
  order?:Order
  done:boolean
}
