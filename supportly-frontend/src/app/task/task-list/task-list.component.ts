import {AfterViewInit, Component} from '@angular/core';
import {
  MatAccordion,
  MatExpansionPanel,
  MatExpansionPanelHeader,
  MatExpansionPanelTitle
} from '@angular/material/expansion';
import {MatButton} from '@angular/material/button';
import {
  MatCell,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderRow,
  MatRow,
  MatTable,
  MatTableDataSource,
  MatTableModule
} from '@angular/material/table';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatCheckbox} from '@angular/material/checkbox';
import {Task} from '../Task';
import {MatDialog} from '@angular/material/dialog';
import {HttpTaskService} from '../service/http-task.service';
import {CommonModule, NgClass, NgIf} from '@angular/common';
import {TaskAddComponent} from '../task-add/task-add.component';
import {OrderCriteria} from '../../order/OrderCriteria';
import {TaskCriteria} from '../TaskCriteria';

@Component({
  selector: 'app-task-list',
  imports: [
    MatAccordion,
    MatButton,
    MatExpansionPanel,
    MatExpansionPanelHeader,
    MatExpansionPanelTitle,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    MatFormField,
    MatCheckbox,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatHeaderRow,
    MatRow, MatTableModule, NgClass, NgIf,  CommonModule
  ],
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.scss'
})
export class TaskListComponent implements AfterViewInit {

  constructor(private dialog: MatDialog, private service: HttpTaskService) {

  }

  ngAfterViewInit() {
    this.service.search({} as TaskCriteria).subscribe({
      next: (task) => {
        this.taskTable.data = task;
      },
      error: (err) => console.error('Błąd przy pobieraniu danych:', err)
    });
  }

  taskColumns: String[] = ['name', 'employee', 'companyName']
  taskTable = new MatTableDataSource<Task>()


  criteriaForm = new FormGroup({
    name: new FormControl(''),
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

  openDialog(): void {
    const dialogRef = this.dialog.open(TaskAddComponent, {
      width: '600px',
      data: {} // opcjonalnie możesz przekazać dane początkowe
    });

    dialogRef.afterClosed().subscribe((result: Task | undefined) => {
      if (result) {
        console.log('Dane z dialogu:', result);
        // tutaj możesz np. wysłać dane do backendu
      }
    });
  }

}
