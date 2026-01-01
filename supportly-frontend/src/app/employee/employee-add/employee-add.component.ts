import {Component, OnInit} from '@angular/core';
import {MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from '@angular/material/dialog';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {MatOption} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';
import {Employee} from '../Employee';
import {HttpEmployeeService} from '../service/http-employee.service';
import {Role} from '../Role';
import {NgForOf} from '@angular/common';
import {HttpRoleService} from '../../role/service/http-role.service';

@Component({
  selector: 'app-employee-add',
  imports: [
    MatDialogContent,
    MatFormField,
    MatDialogActions,
    MatDialogTitle,
    MatInput,
    ReactiveFormsModule,
    MatButton,
    MatLabel,
    MatOption,
    MatSelect,
    NgForOf
  ],
  templateUrl: './employee-add.component.html',
  styleUrl: './employee-add.component.scss',
})
export class EmployeeAddComponent implements OnInit {

  ngOnInit(): void {
    this.loadRoles();
  }

  constructor(
    public dialogRef: MatDialogRef<EmployeeAddComponent>,
    private service: HttpEmployeeService,
    private roleHttp: HttpRoleService,
  ) {
  }

  roles: Role[] = [];

  create = new FormGroup({
    firstName: new FormControl('', Validators.required),
    lastName: new FormControl('', Validators.required),
    phoneNumber: new FormControl('', Validators.required),
    role: new FormControl('', Validators.required),
  });

  submit(): void {
    if (this.create.valid) {
      const formValue = this.create.value as {
        firstName: string;
        lastName: string;
        phoneNumber: string;
        role: string;
      };

      const employee: Partial<Employee> = {
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        phoneNumber: formValue.phoneNumber,
        role: formValue.role as unknown as Role,
      };

      this.service.add(employee).subscribe({
        next: (response) => {
          // np. zamknięcie dialogu z wynikiem
          this.dialogRef.close(response);
        },
        error: (err) => {
          console.error('Błąd przy dodawaniu pracownika', err);
          // możesz tu dodać np. snackbar z błędem
        }
      });

    }
  }

  cancel(): void {
    this.dialogRef.close();
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
