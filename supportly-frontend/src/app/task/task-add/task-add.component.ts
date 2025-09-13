import {Component, CUSTOM_ELEMENTS_SCHEMA, OnInit} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatDialogModule, MatDialogRef} from '@angular/material/dialog';


import {debounceTime, distinctUntilChanged, map, Observable, of, startWith, switchMap} from 'rxjs';
import {Employee} from '../../employee/Employee';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {AsyncPipe, CommonModule} from '@angular/common';
import {MatChipInputEvent, MatChipsModule} from '@angular/material/chips';
import {MatInputModule} from '@angular/material/input';
import {Order} from '../../order/Order';
import {Task} from '../Task';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatButtonModule} from '@angular/material/button';
import {HttpEmployeeService} from '../../employee/service/http-employee.service';
import {HttpOrderService} from '../../order/service/http-order.service';
import {HttpTaskService} from '../service/http-task.service';

@Component({
  selector: 'app-task-add',
  standalone: true,  // <--- to jest kluczowe
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    MatChipsModule,
    MatButtonModule,
    MatCheckboxModule,
    MatDialogModule,
    AsyncPipe      // <-- tutaj musi być

  ],
  templateUrl: './task-add.component.html',
  styleUrl: './task-add.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class TaskAddComponent implements OnInit {

  form = new FormGroup({
    name: new FormControl('', Validators.required),
    employees: new FormControl<Employee[]>([]),
    employeeSearch: new FormControl<Employee | null>(null),
    order: new FormControl<Order | null>(null),
    orderSearch: new FormControl<Order | null>(null),
    done: new FormControl(false)
  });

  filteredEmployees$!: Observable<Employee[]>;
  filteredOrders$!: Observable<Order[]>;

  selectedEmployees: Employee[] = [];

  constructor(
    private dialogRef: MatDialogRef<TaskAddComponent>,
    private employeeService: HttpEmployeeService,
    private orderService: HttpOrderService,
    private service: HttpTaskService
  ) {
  }

  ngOnInit(): void {
    // Autocomplete pracowników
    this.filteredEmployees$ = this.form.get('employeeSearch')!.valueChanges.pipe(
      startWith(null),
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(value => {
        if (!value) return of([]);
        const query = typeof value === 'string' ? value : `${value.firstName} ${value.lastName}`;
        return this.employeeService.searchEmployees(query);
      })
    );

    // Autocomplete zamówień
    this.filteredOrders$ = this.form.get('orderSearch')!.valueChanges.pipe(
      startWith(null),
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(value => {
        if (!value) return of([]);
        const query = typeof value === 'string' ? value : value.client.company.name;
        return this.orderService.searchOrders(query);
      })
    );
  }

  // Wyświetlanie wybranego pracownika w polu input
  displayEmployee(emp: Employee | string | null): string {
    if (!emp) return '';
    console.log(emp)
    return typeof emp === 'string' ? emp : `${emp.firstName} ${emp.lastName}`;
  }

  // Wyświetlanie wybranego zamówienia w polu input
  displayOrder(order: Order | string | null): string {
    if (!order) return '';
    return typeof order === 'string' ? order : `${order.client.company.name} ${order.agreementNumber}`;
  }

  // Wybór zamówienia
  onOrderSelected(order: Order) {
    this.form.patchValue({order});
  }

  // Dodanie pracownika do listy wybranych
  onEmployeeSelected(emp: Employee) {
    if (!this.selectedEmployees.some(e => e.id === emp.id)) {
      this.selectedEmployees.push(emp);
      this.form.patchValue({employees: this.selectedEmployees});
      this.form.get('employeeSearch')!.setValue(null);
    }
  }

  onEmployeeSelectedFromInput(event: MatChipInputEvent) {
    const value = event.value.trim();
    if (value) {
      const emp = this.filteredEmployees$.pipe(
        map(arr => arr.find(e => `${e.firstName} ${e.lastName}` === value))
      );
      emp.subscribe(e => {
        if (e) this.onEmployeeSelected(e);
      });
    }
    event.chipInput!.clear();
  }

  // Usunięcie pracownika z listy
  removeEmployee(emp: Employee) {
    this.selectedEmployees = this.selectedEmployees.filter(e => e.id !== emp.id);
    this.form.patchValue({employees: this.selectedEmployees});
  }

  // Zamknięcie dialogu z przesłaniem danych
  submit() {
    console.log(this.form.getRawValue())
    if (this.form.valid) {
      const formValue = this.form.value as {
        id: number|null
        name: string;
        employees: Employee[];
        order: Order;
        done: boolean;
      };

      const task: Partial<Task> = {
        id: null,
        name: formValue.name,
        employees: formValue.employees,
        order: formValue.order,
        done: formValue.done
      };

      this.service.add(task).subscribe({
        next: (response) => {
          // np. zamknięcie dialogu z wynikiem
          this.dialogRef.close(response);
        },
        error: (err) => {
          console.error('Błąd przy dodawaniu pracownika', err);
          // możesz tu dodać np. snackbar z błędem
        }
      });

      this.dialogRef.close(this.form.value as unknown as Task);
    }
  }

  cancel() {
    this.dialogRef.close();
  }
}
