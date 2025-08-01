import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {MatFormField, MatFormFieldModule} from '@angular/material/form-field';
import {MatInput, MatInputModule} from '@angular/material/input';
import {MatButton, MatButtonModule} from '@angular/material/button';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-part-quantity-dialog',
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatFormField,
    MatInput,
    ReactiveFormsModule,
    MatDialogActions,
    MatButton,
    NgIf,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,

  ],
  templateUrl: './part-quantity-dialog.component.html',
  styleUrl: './part-quantity-dialog.component.scss'
})
export class PartQuantityDialogComponent implements OnInit {
  quantityControl!: FormControl;

  constructor(
    public dialogRef: MatDialogRef<PartQuantityDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  ngOnInit() {
    this.quantityControl = new FormControl(1, [
      Validators.required,
      Validators.min(1),
      Validators.max(this.data.quantity)
    ]);
  }

  submit() {
    this.dialogRef.close(this.quantityControl.value);
  }
}
