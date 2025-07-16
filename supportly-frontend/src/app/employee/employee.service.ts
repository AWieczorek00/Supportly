import {Injectable} from '@angular/core';
import {Employee} from './Employee';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor() {
  }

  criteria(): Employee[] {
    return [
      {
        firstName: 'John Doe',
        lastName: 'Software Engineer',
        email: 'abcd@wp.pl',
        phoneNumber: '123-456-7890',
        username: 'johndoe',
        password: 'password123',
        role: {name: 'Developer'}
      },
      {
        firstName: 'Jane Smith',
        lastName: 'Project Manager',
        email: '',
        phoneNumber: '987-654-3210',
        username: 'janesmith',
        password: 'password456',
        role: {name: 'Manager'}
      },
    ];
  }




}
