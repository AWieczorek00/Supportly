import { TestBed } from '@angular/core/testing';

import { HttpOrderService } from './http-order.service';

describe('HttpOrderService', () => {
  let service: HttpOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HttpOrderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
