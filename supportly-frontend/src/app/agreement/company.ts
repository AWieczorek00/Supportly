import {Address} from './address';

export interface Company {
  name: string;
  nip: string;
  regon: string;
  phoneNumber: string;
  email: string;
  address: Address;
}
