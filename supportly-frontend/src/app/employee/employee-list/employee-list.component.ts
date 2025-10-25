import {AfterViewInit, Component, OnInit} from '@angular/core';
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
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {MatExpansionModule, MatExpansionPanel, MatExpansionPanelTitle} from '@angular/material/expansion';
import {MatButton} from '@angular/material/button';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatFormField, MatFormFieldModule} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {Employee} from '../Employee';
import {EmployeeCriteria} from '../EmployeeCriteria';
import {MatDialog} from '@angular/material/dialog';
import {EmployeeAddComponent} from '../employee-add/employee-add.component';
import {HttpEmployeeService} from '../service/http-employee.service';
import {MatOption, MatSelect} from '@angular/material/select';
import {HttpRoleService} from '../../role/service/http-role.service';
import {Role} from '../Role';

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
    MatExpansionPanel,
    MatExpansionPanelTitle,
    MatExpansionModule,
    MatFormFieldModule,
    MatFormField,
    MatInput,
    ReactiveFormsModule,
    MatButton,
    MatDatepickerModule,
    MatDatepickerModule,
  ],
  templateUrl: './employee-list.component.html',
  styleUrl: './employee-list.component.scss'
})
export class EmployeeListComponent implements AfterViewInit, OnInit {
  constructor(private service: HttpEmployeeService, private dialog: MatDialog, private http: HttpEmployeeService, private roleHttp: HttpRoleService) {
  }

  roles: Role[] = [];
  employeeColumns: string[] = ['firstName', 'lastName', 'phoneNumber', 'role'];
  employeeTable = new MatTableDataSource<Employee>()

  ngAfterViewInit() {
    this.service.search({} as EmployeeCriteria).subscribe({
      next: (employees) => {
        this.employeeTable.data = employees;
      },
      error: (err) => console.error('Błąd przy pobieraniu danych:', err)
    });
  }

  ngOnInit(): void {
    this.loadRoles();
  }

  criteriaForm = new FormGroup({
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    phoneNumber: new FormControl(''),
    role: new FormControl(''),
  });

  onSubmit() {
    const formValue = this.criteriaForm.value;

    const criteria: EmployeeCriteria = {
      firstName: formValue.firstName,
      lastName: formValue.lastName,
      phoneNumber: formValue.phoneNumber,
    };

    this.http.search(criteria).subscribe({
      next: data => {
        console.log('Otrzymane dane:', data);
        this.employeeTable.data = data;

      },
      error: err => {
        console.error('Błąd podczas wyszukiwania:', err);
      }
    });

    // console.log(employeeCriteria);  // Możesz teraz wysłać te dane na serwer lub użyć ich w aplikacji
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

  loadRoles(): void {
    this.roleHttp.allRole().subscribe({
      next: (data: Role[]) => {
        this.roles = data; // Przypisanie listy ról do zmiennej
      },
      error: (err) => {
        console.error('Nie udało się załadować ról:', err);
      }
    });
  }


}
