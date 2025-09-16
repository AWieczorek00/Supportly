import {Employee} from '../employee/Employee';
import {Part} from './Part';
import {Client} from './Client';

export interface Order{
  id:number | null;
  client : Client;
  employeeList: Employee[];
  partList: Part[];
  dateOfAdmission : Date | null;
  dateOfExecution : Date | null;
  agreementNumber : string;
  manHour : number;
  distance : number;
  priority : string;
  status : string;
  note : string;


}
