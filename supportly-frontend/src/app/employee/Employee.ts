import {Role} from './Role';

export interface Employee {
  id:number;
  firstName : string ;
  lastName: string ;
  email: string ;
  phoneNumber: string ;
  username: string ;
  password: string ; // e.g., "Engineering", "Sales", etc.
  role: Role ;
}
