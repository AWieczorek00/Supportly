import { TestBed } from '@angular/core/testing';

import { HttpAgreementService } from './http-agreement.service';

describe('HttpAgreementService', () => {
  let service: HttpAgreementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HttpAgreementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
