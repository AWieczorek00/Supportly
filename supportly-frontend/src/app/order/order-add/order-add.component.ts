import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {MatTab, MatTabGroup} from '@angular/material/tabs';
import {FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatFormField, MatLabel, MatSuffix} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatOption, provideNativeDateAdapter} from '@angular/material/core';
import {MatCard, MatCardActions, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatDivider} from '@angular/material/divider';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
  MatTableDataSource,
  MatTableModule
} from '@angular/material/table';
import {Part} from '../Part';
import {CommonModule} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {MatDialog} from '@angular/material/dialog';
import {PartQuantityDialogComponent} from '../../utils/part-quantity-dialog/part-quantity-dialog.component';
import {MatAutocomplete, MatAutocompleteTrigger} from '@angular/material/autocomplete';
import {Client} from '../Client';
import {debounceTime, distinctUntilChanged, Observable, of, switchMap} from 'rxjs';
import {HttpClientService} from '../../client/service/http-client.service';
import {Priority} from '../../enums/Priority';
import {MatSelect} from '@angular/material/select';
import {Order} from '../Order';
import {HttpOrderService} from '../service/http-order.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-order-add',
  imports: [
    MatTabGroup,
    MatTab,
    ReactiveFormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatSuffix,
    MatCard,
    MatCardTitle,
    MatCardContent,
    MatDivider,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatTable, CommonModule, MatTableModule, MatButton, MatCardActions, MatAutocomplete, MatAutocompleteTrigger, MatOption, MatSelect
  ],
  providers: [provideNativeDateAdapter(),],
  templateUrl: './order-add.component.html',
  styleUrl: './order-add.component.scss'
})
export class OrderAddComponent implements OnInit {

  constructor(private dialog: MatDialog, private service: HttpClientService, private cd: ChangeDetectorRef, private orderService: HttpOrderService,private router: Router) {
  }

  client: Client | undefined;

  ngOnInit() {
    this.filteredClients$ = this.form.get('client')!.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(value => {
        const query = typeof value === 'string' ? value : value?.company?.name;
        if (query && query.length > 1) {
          return this.service.searchCompanies(query); // metoda w serwisie
        }
        return of([]);
      })
    );
  }

  displayClient(client: Client): string {
    return client?.company?.name ?? '';
  }

  onClientSelected(client: Client) {
    console.log('Jestem tutaj');
    if (!client) return;

    this.form.patchValue({
      client: {
        id: client.id != null ? client.id?.toString() : '',
        firstName: client.firstName,
        lastName: client.lastName,
        phoneNumber: client.phoneNumber,
        email: client.email,

        company: {
          name: client.company?.name,
          nip: client.company?.nip ?? '',
          regon: client.company?.regon ?? '',
          phoneNumber: client.company?.phoneNumber ?? '',
          addressEmail: client.company?.email ?? '',

          address: {
            city: client.company?.address?.city ?? '',
            street: client.company?.address?.street ?? '',
            streetNumber: client.company?.address?.streetNumber ?? '',
            // 👇 tutaj zabezpieczamy przed undefined
            apartmentNumber: client.company?.address?.apartmentNumber != null
              ? client.company.address.apartmentNumber.toString()
              : '',
            postalCode: client.company?.address?.zipCode ?? '',
          },
        },
        type: client.type ?? ''
      }
    });
  }

  priorities = Object.values(Priority); // ["LOW","NORMAL","HIGH","CRITICAL"]

  priorityLabels: Record<Priority, string> = {
    [Priority.LOW]: 'Niski',
    [Priority.NORMAL]: 'Normalny',
    [Priority.HIGH]: 'Wysoki',
    [Priority.CRITICAL]: 'Krytyczny'
  };

  filteredClients$!: Observable<Client[]>;

  partColumns: string[] = ['name', 'price', 'tax', 'quantity', 'actions'];
  partTable = new MatTableDataSource<Part>([
    {name: 'Część A', price: 100, tax: 23, quantity: 2, ean: '1234567890123'},
    {name: 'Część B', price: 50, tax: 8, quantity: 5, ean: '9876543210987'},
  ]);
  partTableLocal = new MatTableDataSource<Part>()

  form = new FormGroup({
    client: new FormGroup({
      id: new FormControl(''),
      firstName: new FormControl(''),
      lastName: new FormControl(''),
      phoneNumber: new FormControl(''),
      email: new FormControl('', [Validators.email]),

      company: new FormGroup({
        name: new FormControl(''),
        nip: new FormControl(''),
        regon: new FormControl(''),
        phoneNumber: new FormControl(''),
        addressEmail: new FormControl('', [Validators.email]),
        address: new FormGroup({
          city: new FormControl(''),
          street: new FormControl(''),
          streetNumber: new FormControl(''),
          apartmentNumber: new FormControl(''),
          postalCode: new FormControl(''),
        }),
      }),
      type: new FormControl(''),
    }),
    employeeList: new FormArray([]), // lista FormGroupów reprezentujących Employee
    partList: new FormArray<FormControl<Part>>([]), // ← ważne
    dateOfAdmission: new FormControl<Date | null>(null),
    dateOfExecution: new FormControl<Date | null>(null),
    status: new FormControl(''),
    priority: new FormControl(''),
    distance: new FormControl(0, [Validators.min(0)]),
    manHour: new FormControl(0, [Validators.min(0)]),
    note: new FormControl(''),
  });


  openDialog(part: Part) {
    const dialogRef = this.dialog.open(PartQuantityDialogComponent, {
      data: part
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const copiedPart: Part = {...part, quantity: result};
        this.partTableLocal.data = [...this.partTableLocal.data, copiedPart];
      }
    });
  }

  deletePart(part: Part) {
    this.partTableLocal.data = this.partTableLocal.data.filter(p => p !== part);
  }

  onSubmit() {
// Pobierz dane z formularza
    const formData = this.form.value;

    // Pobierz dane z lokalnej tabeli części
    const localParts = this.partTableLocal.data;

    // Dodaj części do formData jeśli chcesz je dołączyć do struktury formularza
    formData.partList = localParts;

    if (this.form.valid) {
      const raw = this.form.value;

      const order: Order = {
        id: null, // zakładam, że backend sam nadaje ID
        client: {
          id: raw.client?.id != null ? Number(raw.client.id) : null,
          firstName: raw.client?.firstName ?? '',
          lastName: raw.client?.lastName ?? '',
          phoneNumber: raw.client?.phoneNumber ?? '',
          email: raw.client?.email ?? '',
          type: raw.client?.type ?? '',
          company: {
            name: raw.client?.company?.name ?? '',
            nip: raw.client?.company?.nip ?? '',
            regon: raw.client?.company?.regon ?? '',
            phoneNumber: raw.client?.company?.phoneNumber ?? '',
            email: raw.client?.company?.addressEmail ?? '',
            address: {
              city: raw.client?.company?.address?.city ?? '',
              street: raw.client?.company?.address?.street ?? '',
              streetNumber: raw.client?.company?.address?.streetNumber ?? '',
              apartmentNumber: raw.client?.company?.address?.apartmentNumber
                ? Number(raw.client?.company?.address?.apartmentNumber)
                : 0,
              zipCode: raw.client?.company?.address?.postalCode ?? ''
            }
          }
        },
        employeeList: raw.employeeList ?? [],
        partList: raw.partList ?? [],
        dateOfAdmission: raw.dateOfAdmission ? new Date(raw.dateOfAdmission) : null,
        dateOfExecution: raw.dateOfExecution ? new Date(raw.dateOfExecution) : null,
        agreementNumber: '',
        manHour: raw.manHour ? Number(raw.manHour) : 0,
        distance: raw.distance ? Number(raw.distance) : 0,
        priority: raw.priority ?? 'NORMAL',
        status: raw.status ?? 'NEW',
        note: raw.note ?? ''
      };

      this.orderService.add(order).subscribe({
        next: (res) => {
          console.log('Zlecenie dodane:', res);
          this.router.navigate(['/order/list']);
          this.form.reset();
        },
        error: (err) => {
          console.error('Błąd przy dodawaniu zlecenia:', err);
        }
      });
    }

    // Wyświetl w konsoli pełne dane
    console.log('Formularz:', formData);

    // Ewentualnie tylko części
    console.log('Części (z partTableLocal):', localParts);  }
}

