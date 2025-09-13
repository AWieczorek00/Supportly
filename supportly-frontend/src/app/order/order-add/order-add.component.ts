import {Component} from '@angular/core';
import {MatTab, MatTabGroup} from '@angular/material/tabs';
import {FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatFormField, MatLabel, MatSuffix} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {provideNativeDateAdapter} from '@angular/material/core';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatDivider} from '@angular/material/divider';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
  MatTableDataSource,
  MatTableModule
} from '@angular/material/table';
import {Part} from '../Part';
import {CommonModule} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {MatDialog} from '@angular/material/dialog';
import {PartQuantityDialogComponent} from '../../utils/part-quantity-dialog/part-quantity-dialog.component';

@Component({
  selector: 'app-order-add',
  imports: [
    MatTabGroup,
    MatTab,
    ReactiveFormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatSuffix,
    MatCard,
    MatCardTitle,
    MatCardContent,
    MatDivider,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatTable, CommonModule, MatTableModule, MatButton, MatCardActions
  ],
  providers: [provideNativeDateAdapter(),],
  templateUrl: './order-add.component.html',
  styleUrl: './order-add.component.scss'
})
export class OrderAddComponent {

  constructor(private dialog: MatDialog) {
  }


  partColumns: string[] = ['name', 'price', 'tax', 'quantity', 'actions'];
  partTable = new MatTableDataSource<Part>([
    {name: 'Część A', price: 100, tax: 23, quantity: 2, ean: '1234567890123'},
    {name: 'Część B', price: 50, tax: 8, quantity: 5, ean: '9876543210987'},
  ]);
  partTableLocal = new MatTableDataSource<Part>()

  form = new FormGroup({
    client: new FormGroup({
      firstName: new FormControl(''),
      lastName: new FormControl(''),
      phoneNumber: new FormControl(''),
      email: new FormControl('', [Validators.email]),

      company: new FormGroup({
        name: new FormControl(''),
        nip: new FormControl(''),
        regon: new FormControl(''),
        phoneNumber: new FormControl(''),
        addressEmail: new FormControl('', [Validators.email]),
        address: new FormGroup({
          city: new FormControl(''),
          street: new FormControl(''),
          streetNumber: new FormControl(''),
          apartmentNumber: new FormControl(''),
          postalCode: new FormControl(''),
        }),
        type: new FormControl(''),
      })
    }),
    employeeList: new FormArray([]), // lista FormGroupów reprezentujących Employee
    partList: new FormArray<FormControl<Part>>([]), // ← ważne
    dateOfAdmission: new FormControl<Date | null>(null),
    dateOfExecution: new FormControl<Date | null>(null),
    status: new FormControl(''),
    priority: new FormControl(''),
    distance: new FormControl(0, [Validators.min(0)]),
    manHour: new FormControl(0, [Validators.min(0)]),
    note: new FormControl(''),
  });


  openDialog(part: Part) {
    const dialogRef = this.dialog.open(PartQuantityDialogComponent, {
      data: part
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const copiedPart: Part = {...part, quantity: result};
        this.partTableLocal.data = [...this.partTableLocal.data, copiedPart];
      }
    });
  }

  deletePart(part: Part) {
    this.partTableLocal.data = this.partTableLocal.data.filter(p => p !== part);
  }

  onSubmit() {
// Pobierz dane z formularza
    const formData = this.form.value;

    // Pobierz dane z lokalnej tabeli części
    const localParts = this.partTableLocal.data;

    // Dodaj części do formData jeśli chcesz je dołączyć do struktury formularza
    formData.partList = localParts;

    // Wyświetl w konsoli pełne dane
    console.log('Formularz:', formData);

    // Ewentualnie tylko części
    console.log('Części (z partTableLocal):', localParts);
  }
}

