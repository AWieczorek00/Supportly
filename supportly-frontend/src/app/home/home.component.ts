import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {MatGridList, MatGridTile} from '@angular/material/grid-list';
import {MatPaginator} from '@angular/material/paginator';
import {
  MatCell, MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef, MatTable, MatTableDataSource
} from '@angular/material/table';
import {MaintenanceTask} from './maintenance-task/maintenance-task';
import {MaintenanceTaskService} from './maintenance-task/maintenance-task.service';
import {DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {MatCheckbox} from '@angular/material/checkbox';
import {Task} from '../task/Task';
import {HttpTaskService} from '../task/service/http-task.service';
import {BaseComponent} from '../config/base-component';
import {TaskCriteria} from '../task/TaskCriteria';

@Component({
  selector: 'app-home',
  imports: [
    MatGridList,
    MatGridTile,
    MatPaginator,
    MatHeaderRow,
    MatRow,
    MatRowDef,
    MatHeaderRowDef,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatCellDef,
    MatHeaderCellDef,
    MatTable,
    DatePipe,
    MatButton,
    MatCheckbox,
    NgForOf,
    NgIf,
    NgClass
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent extends BaseComponent implements AfterViewInit {



  constructor(
    private services: MaintenanceTaskService,
    private service: HttpTaskService) {
    super();
  }

  displayedColumns: string[] = ['position', 'name', 'weight', 'symbol'];
  maintenanceTaskColumns: string[] = ['id', 'companyName', 'executionTime', 'done','action'];
  dataSource = new MatTableDataSource<PeriodicElement>(ELEMENT_DATA);
  maintenanceTask = new MatTableDataSource<MaintenanceTask>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.maintenanceTask.data = this.services.getAll();
    // console.log(this.maintenanceTask.data);
    console.log(this.getUserId())

    this.service.getTaskForEmployee(this.getUserId()).subscribe({
      next: (task) => {
        this.taskTable.data = task;
      },
      error: (err) => console.error('Błąd przy pobieraniu danych:', err)
    });
  }


  taskColumns: String[] = ['name', 'employee', 'companyName','done']
  taskTable = new MatTableDataSource<Task>()



  onToggleDone(task: Task) {
    const updatedTask = { ...task, done: !task.done };
    this.service.updateDone(updatedTask).subscribe({
      next: (res) => {
        // podmiana obiektu w tabeli
        const index = this.taskTable.data.findIndex(t => t.id === res.id);
        if (index !== -1) {
          this.taskTable.data[index] = res;
          this.taskTable._updateChangeSubscription(); // odśwież tabelę
        }
      },
      error: (err) => {
        console.error('Błąd przy update', err);
      }
    });
  }









}

export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}



const ELEMENT_DATA: PeriodicElement[] = [
  {position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H'},
  {position: 2, name: 'Helium', weight: 4.0026, symbol: 'He'},
  {position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li'},
  {position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be'},
  {position: 5, name: 'Boron', weight: 10.811, symbol: 'B'},
  {position: 6, name: 'Carbon', weight: 12.0107, symbol: 'C'},
  {position: 7, name: 'Nitrogen', weight: 14.0067, symbol: 'N'},
  {position: 8, name: 'Oxygen', weight: 15.9994, symbol: 'O'},
  {position: 9, name: 'Fluorine', weight: 18.9984, symbol: 'F'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 11, name: 'Sodium', weight: 22.9897, symbol: 'Na'},
  {position: 12, name: 'Magnesium', weight: 24.305, symbol: 'Mg'},
  {position: 13, name: 'Aluminum', weight: 26.9815, symbol: 'Al'},
  {position: 14, name: 'Silicon', weight: 28.0855, symbol: 'Si'},
  {position: 15, name: 'Phosphorus', weight: 30.9738, symbol: 'P'},
  {position: 16, name: 'Sulfur', weight: 32.065, symbol: 'S'},
  {position: 17, name: 'Chlorine', weight: 35.453, symbol: 'Cl'},
  {position: 18, name: 'Argon', weight: 39.948, symbol: 'Ar'},
  {position: 19, name: 'Potassium', weight: 39.0983, symbol: 'K'},
  {position: 20, name: 'Calcium', weight: 40.078, symbol: 'Ca'},
];


