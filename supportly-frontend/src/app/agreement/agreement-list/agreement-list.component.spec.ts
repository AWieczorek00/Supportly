import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgreementListComponent } from './agreement-list.component';

describe('AgreementListComponent', () => {
  let component: AgreementListComponent;
  let fixture: ComponentFixture<AgreementListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgreementListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgreementListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
