import { TestBed } from '@angular/core/testing';

import { HttpRoleService } from './http-role.service';

describe('HttpRoleService', () => {
  let service: HttpRoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HttpRoleService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
