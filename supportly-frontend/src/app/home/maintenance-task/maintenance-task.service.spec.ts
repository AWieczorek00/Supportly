import { TestBed } from '@angular/core/testing';

import { MaintenanceTaskService } from './maintenance-task.service';

describe('MaintenanceTaskService', () => {
  let service: MaintenanceTaskService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaintenanceTaskService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
