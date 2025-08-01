import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartQuantityDialogComponent } from './part-quantity-dialog.component';

describe('PartQuantityDialogComponent', () => {
  let component: PartQuantityDialogComponent;
  let fixture: ComponentFixture<PartQuantityDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PartQuantityDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PartQuantityDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
