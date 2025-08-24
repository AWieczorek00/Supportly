import { TestBed } from '@angular/core/testing';

import { HttpTaskService } from './http-task.service';

describe('HttpTaskService', () => {
  let service: HttpTaskService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HttpTaskService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
