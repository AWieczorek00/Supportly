export interface OrderCriteria{
  nameCompany?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  phoneNumber?: string | null;
  email?: string | null;
  dateOfAdmissionFrom?: Date;
  dateOfAdmissionTo?: Date;
  dateOfExecutionFrom?: Date;
  dateOfExecutionTo?: Date;
  status?: string | null;
  priority?: string;
}
