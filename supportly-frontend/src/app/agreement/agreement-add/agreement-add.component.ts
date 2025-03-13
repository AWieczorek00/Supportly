import {AfterViewInit, ChangeDetectionStrategy, Component} from '@angular/core';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';
import {CommonModule, DatePipe} from '@angular/common';
import {Agreement} from '../agreement';
import {AgreementService} from '../agreement.service';
import {MatExpansionModule, MatExpansionPanel, MatExpansionPanelTitle} from '@angular/material/expansion';
import {MatFormField, MatFormFieldModule} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {
  MatDatepicker,
  MatDatepickerInput,
  MatDatepickerModule,
  MatDatepickerToggle
} from '@angular/material/datepicker';
import {DateAdapter, MAT_DATE_FORMATS, NativeDateAdapter} from '@angular/material/core';
import {AgreementCriteria} from '../agreement-criteria';

export const MY_DATE_FORMATS = {
  parse: {
    dateInput: 'YYYY-MM-DD',  // Format dla parsowania daty z inputa
  },
  display: {
    dateInput: 'YYYY-MM-DD',  // Format wyświetlania daty w inputie
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};


@Component({
  selector: 'app-agreement-add',
  imports: [
    CommonModule,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatHeaderCellDef,
    MatCellDef,
    MatHeaderRow,
    MatRow,
    MatHeaderRowDef,
    MatRowDef,
    DatePipe,
    MatExpansionPanel,
    MatExpansionPanelTitle,
    MatExpansionModule,
    MatFormFieldModule,
    MatFormField,
    MatInput,
    ReactiveFormsModule,
    MatButton,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatDatepicker,
    MatDatepickerModule

  ],
  providers: [
    {provide: DateAdapter, useClass: NativeDateAdapter},
    {provide: MAT_DATE_FORMATS, useValue: MY_DATE_FORMATS},  // Dodanie dostawcy formatów daty

  ],
  templateUrl: './agreement-add.component.html',
  styleUrl: './agreement-add.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,

})
export class AgreementAddComponent {
  // agreementForm: FormGroup;


  agreementForm = new FormGroup({
    name: new FormControl(''),
    nip: new FormControl(''),
    regon: new FormControl(''),
    phoneNumber: new FormControl(''),
    addressEmail: new FormControl('', Validators.email),

    city: new FormControl(''),
    street: new FormControl(''),
    buildingNumber: new FormControl(''),
    apartmentNumber: new FormControl(''),
    postalCode: new FormControl(''),

    costForServicePerHour: new FormControl(''),
    numberAgreement: new FormControl(''),
    dateFrom: new FormControl(''),
    dateTo: new FormControl(''),
    period: new FormControl('')

  });

  submitForm() {
    if (this.agreementForm.valid) {
      console.log(this.agreementForm.value);
    }
  }

}
