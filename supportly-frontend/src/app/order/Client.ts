import {Company} from '../agreement/company';

export interface Client{
  firstName: string;
  lastName: string;
  phoneNumber: string;
  email: string;
  company : Company;
  type: string;

}
