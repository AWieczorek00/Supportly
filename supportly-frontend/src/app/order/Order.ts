import {Employee} from '../employee/Employee';
import {Part} from './Part';
import {Client} from './Client';

export interface Order{
  id:number;
  client : Client;
  employeeList: Employee[];
  partList: Part[];
  dateOfAdmission : Date;
  dateOfExecution : Date;
  agreementNumber : string;
  manHour : number;
  distance : number;
  priority : string;
  status : string;
  note : string;


}
