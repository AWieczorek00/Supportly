import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgreementAddComponent } from './agreement-add.component';

describe('AgreementAddComponent', () => {
  let component: AgreementAddComponent;
  let fixture: ComponentFixture<AgreementAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgreementAddComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgreementAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
