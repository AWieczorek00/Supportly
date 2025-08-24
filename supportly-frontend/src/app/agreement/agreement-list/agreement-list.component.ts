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
import {RouterLink} from '@angular/router';
import {HttpAgreementService} from '../service/http-agreement.service';

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
  selector: 'app-agreement-list',
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
    MatDatepickerModule,
    RouterLink
  ],
  providers: [
    {provide: DateAdapter, useClass: NativeDateAdapter},
    {provide: MAT_DATE_FORMATS, useValue: MY_DATE_FORMATS},  // Dodanie dostawcy formatów daty

  ],
  templateUrl: './agreement-list.component.html',
  styleUrl: './agreement-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AgreementListComponent implements AfterViewInit {
  constructor(private service: HttpAgreementService) {
  }

  agreementColumns: string[] = ['name', 'dateFrom', 'dateTo', 'nip', 'addressEmail', 'city', 'street', 'number', 'period'];
  agreementTable = new MatTableDataSource<Agreement>()

  // accordion = viewChild.required(MatAccordion);

  ngAfterViewInit() {
    this.service.search({} as AgreementCriteria).subscribe({
      next: (agreements) => {
        this.agreementTable.data = agreements;
      },
      error: (err) => console.error('Błąd przy pobieraniu danych:', err)
    });
  }

  registrationForm = new FormGroup({
    name: new FormControl(''),
    nip: new FormControl(''),
    email: new FormControl('', Validators.email),
    dateFrom: new FormControl(''),
    dateTo: new FormControl(''),
    period: new FormControl('')
  });


  onSubmit() {
    const formValue = this.registrationForm.value;

    const agreementCriteria: AgreementCriteria = {
      name: formValue.name ?? null,  // Zapewniamy, że nie będzie undefined
      dateFrom: this.convertToDate(formValue.dateFrom ?? null),  // Zapewniamy, że nie będzie undefined
      dateTo: this.convertToDate(formValue.dateTo ?? null),  // Zapewniamy, że nie będzie undefined
      period: formValue.period ? Number(formValue.period) : null,  // Jeśli brak wartości, przypisujemy null
      nip: formValue.nip ?? null,
      email: formValue.email ?? null
    };

    this.service.search(agreementCriteria).subscribe({
      next: (agreements: Agreement[]) => {
        this.agreementTable.data = agreements; // teraz to już zwykła tablica
      },
      error: (err) => {
        console.error('Błąd przy pobieraniu danych:', err);
      }
    });    console.log(agreementCriteria);  // Możesz teraz wysłać te dane na serwer lub użyć ich w aplikacji
  }

// Funkcja pomocnicza do konwersji daty
  private convertToDate(date: string | Date | null): Date | null {
    if (!date) {
      return null;  // Zwraca null, jeśli brak daty lub null
    }
    return (date instanceof Date) ? date : new Date(date);
  }


  onClear(): void {
    this.registrationForm.reset(); // Resetuje formularz
  }


}
