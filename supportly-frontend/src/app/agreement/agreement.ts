import {Company} from './company';

export interface Agreement {
  company: Company;
  dateFrom: Date;
  dateTo: Date;
  period: number;
  costForServicePerHour: number;
  numberAgreement: string;
}
