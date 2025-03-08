import {Injectable} from '@angular/core';
import {MaintenanceTask} from './maintenance-task';

@Injectable({
  providedIn: 'root'
})
export class MaintenanceTaskService {

  constructor() {
  }

  getAll(): MaintenanceTask[] {
    return [
      { id: 1, companyName: 'Company A', executionTime: new Date('2023-01-01'), done: false },
      { id: 2, companyName: 'Company B', executionTime: new Date('2023-02-01'), done: true },
      { id: 3, companyName: 'Company C', executionTime: new Date('2023-03-01'), done: false }
    ];
  }
}
