import {Component} from '@angular/core';
import {MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from '@angular/material/dialog';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {MatOption} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';

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
    MatSelect
  ],
  templateUrl: './employee-add.component.html',
  styleUrl: './employee-add.component.scss',
})
export class EmployeeAddComponent {

  constructor(
    public dialogRef: MatDialogRef<EmployeeAddComponent>
  ) {
  }

  create = new FormGroup({
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    phoneNumber: new FormControl(''),
    email: new FormControl(''),
    username: new FormControl(''),
    password: new FormControl(''),
    role: new FormControl(''),
  });

  submit(): void {
    if (this.create.valid) {
      this.dialogRef.close(this.create.value);
    }
  }

  cancel(): void {
    this.dialogRef.close();
  }
}
