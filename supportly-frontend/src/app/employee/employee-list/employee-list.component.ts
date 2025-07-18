import {AfterViewInit, Component} from '@angular/core';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef, MatTable,
  MatTableDataSource
} from '@angular/material/table';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {CommonModule, DatePipe, NgIf} from '@angular/common';
import {
  MatAccordion, MatExpansionModule,
  MatExpansionPanel,
  MatExpansionPanelHeader,
  MatExpansionPanelTitle
} from '@angular/material/expansion';
import {MatButton} from '@angular/material/button';
import {
  MatDatepicker,
  MatDatepickerInput,
  MatDatepickerModule,
  MatDatepickerToggle
} from '@angular/material/datepicker';
import {MatError, MatFormField, MatFormFieldModule, MatHint, MatLabel, MatSuffix} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {RouterLink} from '@angular/router';
import {Employee} from '../Employee';
import {EmployeeService} from '../employee.service';
import {AgreementCriteria} from '../../agreement/agreement-criteria';
import {EmployeeCriteria} from '../EmployeeCriteria';
import {MatDialog} from '@angular/material/dialog';
import {EmployeeAddComponent} from '../employee-add/employee-add.component';

@Component({
  selector: 'app-employee-list',
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
  templateUrl: './employee-list.component.html',
  styleUrl: './employee-list.component.scss'
})
export class EmployeeListComponent implements AfterViewInit {

  constructor(private service: EmployeeService,private dialog: MatDialog) {

  }

  employeeColumns: string[] = ['firstName', 'lastName', 'phoneNumber', 'role'];
  employeeTable = new MatTableDataSource<Employee>()

  ngAfterViewInit() {
    this.employeeTable.data = this.service.criteria();
  }

  criteriaForm = new FormGroup({
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    phoneNumber: new FormControl(''),
    role: new FormControl(''),
  });

  onSubmit() {
    const formValue = this.criteriaForm.value;

    const employeeCriteria: EmployeeCriteria = {
      firstName: formValue.firstName,
      lastName: formValue.lastName,
      phoneNumber: formValue.phoneNumber,
      role: formValue.role
    };

    this.employeeTable.data = this.service.criteria() // Zmienic jak bede miał backend
    console.log(employeeCriteria);  // Możesz teraz wysłać te dane na serwer lub użyć ich w aplikacji
  }

  onClear(): void {
    this.criteriaForm.reset(); // Resetuje formularz
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(EmployeeAddComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('Dane z formularza:', result);
      }
    });
  }

}
