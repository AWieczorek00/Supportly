import {Employee} from '../employee/Employee';
import {Part} from './Part';
import {Client} from './Client';

export interface Order{
  client : Client;
  employeeList: Employee[];
  partList: Part[];
  dateOfAdmission : Date;
  dateOfExecution : Date;
  manHour : number;
  distance : number;
  priority : string;
  status : string;
  note : string;


}
