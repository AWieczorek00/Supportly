import {Company} from '../agreement/company';

export interface Client{
  id:number|null;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  email: string;
  company : Company;
  type: string;

}
