import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatExpansionModule, MatExpansionPanel, MatExpansionPanelTitle} from '@angular/material/expansion';
import {MatFormField, MatFormFieldModule} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {DateAdapter, MAT_DATE_FORMATS, MatOption, NativeDateAdapter} from '@angular/material/core';
import {MatAutocomplete, MatAutocompleteTrigger} from '@angular/material/autocomplete';
import {debounceTime, distinctUntilChanged, Observable, of, switchMap} from 'rxjs';
import { Company } from '../company';
import * as http from 'node:http';
import {HttpClientService} from '../../client/service/http-client.service';
import {Client} from '../../order/Client';
import {HttpAgreementService} from '../service/http-agreement.service';
import {Agreement} from '../agreement';
import {Router} from '@angular/router';

export const MY_DATE_FORMATS = {
  parse: {
    dateInput: 'YYYY-MM-DD',  // Format dla parsowania daty z inputa
  },
  display: {
    dateInput: 'YYYY-MM-DD',  // Format wyświetlania daty w inputie
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};


@Component({
  selector: 'app-agreement-add',
  imports: [
    CommonModule,
    MatExpansionPanel,
    MatExpansionPanelTitle,
    MatExpansionModule,
    MatFormFieldModule,
    MatFormField,
    MatInput,
    ReactiveFormsModule,
    MatButton,
    MatDatepickerModule,
    MatAutocompleteTrigger,
    MatAutocomplete,
    MatOption

  ],
  providers: [
    {provide: DateAdapter, useClass: NativeDateAdapter},
    {provide: MAT_DATE_FORMATS, useValue: MY_DATE_FORMATS},  // Dodanie dostawcy formatów daty

  ],
  templateUrl: './agreement-add.component.html',
  styleUrl: './agreement-add.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,

})
export class AgreementAddComponent implements OnInit {


  constructor(private service: HttpClientService,private cd: ChangeDetectorRef, private agreementService:HttpAgreementService,private router: Router) {}

  agreementForm = new FormGroup({
    name: new FormControl(''),
    nip: new FormControl(''),
    regon: new FormControl(''),
    phoneNumber: new FormControl(''),
    addressEmail: new FormControl('', Validators.email),

    city: new FormControl(''),
    street: new FormControl(''),
    buildingNumber: new FormControl(''),
    apartmentNumber: new FormControl(''),
    postalCode: new FormControl(''),

    costForServicePerHour: new FormControl(''),
    agreementNumber: new FormControl(''),
    dateFrom: new FormControl(''),
    dateTo: new FormControl(''),
    period: new FormControl(''),

    client: new FormControl<Client | null>(null)

  });

  submitForm() {
    if (this.agreementForm.valid) {
      const raw = this.agreementForm.value;

      const agreement: Agreement = {
        company: raw.client?.company!,   // zakładam, że w polu `client` masz obiekt typu Company
        dateFrom: raw.dateFrom ? new Date(raw.dateFrom) : new Date(),
        dateTo: raw.dateTo ? new Date(raw.dateTo) : new Date(),
        period: raw.period ? Number(raw.period) : 0,
        costForServicePerHour: raw.costForServicePerHour ? Number(raw.costForServicePerHour) : 0,
        agreementNumber: raw.agreementNumber ?? '',
        signedDate: new Date()  // możesz brać z formularza, jeśli dodasz pole signedDate
      };

      this.agreementService.add(agreement).subscribe({
        next: (res) => {
          console.log('Umowa dodana:', res);
          this.router.navigate(['/agreement/list']);
        },
        error: (err) => {
          console.error('Błąd przy dodawaniu umowy:', err);
        }
      });
    }
  }

  filteredClients$!: Observable<Client[]>;          // ✅ jawny typ


  ngOnInit() {
    this.filteredClients$ = this.agreementForm.get('client')!.valueChanges.pipe(
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
    if (!client) return;


    // wypełnianie pól formularza na podstawie wybranego klienta
    this.agreementForm.patchValue({
      name: client.company.name,
      nip: client.company.nip ?? '',
      regon: client.company.regon ?? '',
      phoneNumber: client.company.phoneNumber ?? '',
      addressEmail: client.company.email ?? '',
      city: client.company.address?.city ?? '',
      street: client.company.address?.street ?? '',
      // buildingNumber: client.company.address?.buildingNumber ?? '',
      apartmentNumber: client.company.address?.apartmentNumber?.toString() ?? '',
      postalCode: client.company.address?.zipCode ?? ''
    });

    console.log('After patchValue:', this.agreementForm.value);
    this.cd.detectChanges(); // wymusza aktualizację widoku

  }

}
