import {Injectable} from '@angular/core';
import {Agreement} from './agreement';
import {AgreementCriteria} from './agreement-criteria';

@Injectable({
  providedIn: 'root'
})
export class AgreementService {
  constructor() {
  }

  criteria2(criteria: AgreementCriteria): Agreement[] {
    return [
      {
        company: {
          name: 'GreenTech Solutions Inc.',
          nip: '1122334455',
          regon: '6677889900',
          phoneNumber: '321654987',
          addressEmail: 'contact@greentechsolutions.com',
          address: {
            city: 'Wrocław',
            street: 'Zielona',
            streetNumber: '33',
            apartmentNumber: 3,
            zipCode: '50-123'
          }
        },
        dateFrom: new Date('2023-03-10'),
        dateTo: new Date('2024-03-10'),
        costForServicePerHour: 120,
        numberAgreement: 'GTS/2023/03/10',
        period: 12
      },
      {
        company: {
          name: 'Smart Innovations Ltd.',
          nip: '3344556677',
          regon: '1122334455',
          phoneNumber: '654987321',
          addressEmail: 'info@smartinnovations.com',
          address: {
            city: 'Poznań',
            street: 'Nowa',
            streetNumber: '48',
            apartmentNumber: 2,
            zipCode: '60-001'
          }
        },
        dateFrom: new Date('2022-06-01'),
        dateTo: new Date('2025-06-01'),
        costForServicePerHour: 180,
        numberAgreement: 'SI/2022/06/01',
        period: 24
      },
      {
        company: {
          name: 'TechWorks S.A.',
          nip: '9988776655',
          regon: '2233445566',
          phoneNumber: '123987654',
          addressEmail: 'contact@techworks.com',
          address: {
            city: 'Katowice',
            street: 'Księżnej',
            streetNumber: '55',
            apartmentNumber: 1,
            zipCode: '40-123'
          }
        },
        dateFrom: new Date('2023-09-01'),
        dateTo: new Date('2026-09-01'),
        costForServicePerHour: 200,
        numberAgreement: 'TW/2023/09/01',
        period: 36
      },
      {
        company: {
          name: 'BlueWave Technologies',
          nip: '7788996655',
          regon: '6677882233',
          phoneNumber: '890765432',
          addressEmail: 'contact@bluewavetech.com',
          address: {
            city: 'Lublin',
            street: 'Rynkowska',
            streetNumber: '9',
            apartmentNumber: 10,
            zipCode: '20-345'
          }
        },
        dateFrom: new Date('2023-12-15'),
        dateTo: new Date('2024-12-15'),
        costForServicePerHour: 110,
        numberAgreement: 'BWT/2023/12/15',
        period: 12
      },
      {
        company: {
          name: 'Innovative Systems Inc.',
          nip: '1239876543',
          regon: '4561239876',
          phoneNumber: '321654987',
          addressEmail: 'contact@innovativesystems.com',
          address: {
            city: 'Szczecin',
            street: 'Przemysłowa',
            streetNumber: '22',
            apartmentNumber: 8,
            zipCode: '70-234'
          }
        },
        dateFrom: new Date('2024-01-10'),
        dateTo: new Date('2025-01-10'),
        costForServicePerHour: 140,
        numberAgreement: 'IS/2024/01/10',
        period: 12
      }
    ];
  }

  criteria(): Agreement[] {
    return [
      {
        company: {
          name: 'Tech Solutions Sp. z o.o.',
          nip: '1234567890',
          regon: '0987654321',
          phoneNumber: '123456789',
          addressEmail: 'kontakt@techsolutions.pl',
          address: {
            city: 'Warszawa',
            street: 'Kwiatowa',
            streetNumber: '12A',
            apartmentNumber: 5,
            zipCode: '00-123'
          }
        },
        dateFrom: new Date("2023-01-01"),
        dateTo: new Date('2024-01-01'),
        costForServicePerHour: 100,
        numberAgreement: 'TS/2023/01/01',
        period: 12
      },
      {
        company: {
          name: 'InnoSoft S.A.',
          nip: '9876543210',
          regon: '0123456789',
          phoneNumber: '987654321',
          addressEmail: 'info@innosoft.com',
          address: {
            city: 'Kraków',
            street: 'Długa',
            streetNumber: '25',
            apartmentNumber: 1,
            zipCode: '30-123'
          }
        },
        dateFrom: new Date('2022-05-15'),
        dateTo: new Date('2025-05-15'),
        costForServicePerHour: 150,
        numberAgreement: 'IS/2022/05/15',
        period: 6
      },
      {
        company: {
          name: 'FutureTech Ltd.',
          nip: '4561237890',
          regon: '7894561230',
          phoneNumber: '456123789',
          addressEmail: 'support@futuretech.com',
          address: {
            city: 'Gdańsk',
            street: 'Morska',
            streetNumber: '7',
            apartmentNumber: 5,
            zipCode: '80-123'
          }
        },
        dateFrom: new Date('2024-02-01'),
        dateTo: new Date('2026-02-01'),
        period: 3,
        costForServicePerHour: 200,
        numberAgreement: 'FT/2024/02/01'
      }
    ];
  }
}
