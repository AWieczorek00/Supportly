import { Component } from '@angular/core';
import {
  MatAccordion,
  MatExpansionPanel,
  MatExpansionPanelHeader,
  MatExpansionPanelTitle
} from '@angular/material/expansion';
import {MatButton} from '@angular/material/button';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow, MatRowDef, MatTable
} from '@angular/material/table';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatCheckbox} from '@angular/material/checkbox';

@Component({
  selector: 'app-task-list',
  imports: [
    MatAccordion,
    MatButton,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatExpansionPanel,
    MatExpansionPanelHeader,
    MatExpansionPanelTitle,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatInput,
    MatLabel,
    MatRow,
    MatRowDef,
    MatTable,
    ReactiveFormsModule,
    MatFormField,
    MatCheckbox
  ],
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.scss'
})
export class TaskListComponent {

  task


  criteriaForm = new FormGroup({
    name : new FormControl(''),
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    companyName: new FormControl(''),
    done: new FormControl(false)
  });


  onSubmit() {

  }

  onClear(): void {
    this.criteriaForm.reset(); // Resetuje formularz
  }
}
