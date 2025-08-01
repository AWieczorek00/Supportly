import { Component } from '@angular/core';
import {
  MatCell, MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef, MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';
import {Order} from '../Order';
import {CommonModule, DatePipe} from '@angular/common';
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
import {RouterLink} from '@angular/router';
import {EmployeeCriteria} from '../../employee/EmployeeCriteria';
import {provideNativeDateAdapter} from '@angular/material/core';
import {OrderCriteria} from '../OrderCriteria';
import {OrderService} from '../order.service';

@Component({
  selector: 'app-order-list',
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
    RouterLink,
  ],
  providers: [  provideNativeDateAdapter(),],
  templateUrl: './order-list.component.html',
  styleUrl: './order-list.component.scss'
})
export class OrderListComponent {

  constructor(private service: OrderService) {

  }


  orderColumns: string[] = ['companyName', "clientName", 'phoneNumber', 'email', 'dateOfAdmission', 'dateOfExecution','status'];
  orderTable = new MatTableDataSource<Order>()

  criteriaForm = new FormGroup({
    companyName: new FormControl(''),
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    phoneNumber: new FormControl(''),
    email: new FormControl('', [Validators.email]),
    dateOfAdmissionTo: new FormControl(''),
    dateOfAdmissionFrom: new FormControl(''),
    dateOfExecutionTo: new FormControl(''),
    dateOfExecutionFrom: new FormControl(''),
    status: new FormControl(''),
  });

  onClear(): void {
    this.criteriaForm.reset(); // Resetuje formularz
  }

  onSubmit() {
    const formValue = this.criteriaForm.value;

    const orderCriteria: OrderCriteria = {
        nameCompany: formValue.companyName,
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        phoneNumber: formValue.phoneNumber,
        email: formValue.email,
        dateOfAdmissionFrom: formValue.dateOfAdmissionFrom ? new Date(formValue.dateOfAdmissionFrom) : undefined,
        dateOfAdmissionTo: formValue.dateOfAdmissionTo ? new Date(formValue.dateOfAdmissionTo) : undefined,
        dateOfExecutionFrom: formValue.dateOfExecutionFrom ? new Date(formValue.dateOfExecutionFrom) : undefined,
        dateOfExecutionTo: formValue.dateOfExecutionTo ? new Date(formValue.dateOfExecutionTo) : undefined,
        status: formValue.status

    };

    this.orderTable.data = this.service.criteria() // Zmienic jak bede miał backend
    console.log(orderCriteria);  // Możesz teraz wysłać te dane na serwer lub użyć ich w aplikacji
  }
}
