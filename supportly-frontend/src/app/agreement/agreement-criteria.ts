export interface AgreementCriteria {
  name?: string | null;  // Możliwe wartości: string, null lub undefined
  dateFrom?: Date | string | null;  // Możliwe wartości: Date, string, null lub undefined
  dateTo?: Date | string | null;  // Możliwe wartości: Date, string, null lub undefined
  period?: number | null;  // Możliwe wartości: number lub null
  nip?: string | null;  // Możliwe wartości: string lub null
  email?: string | null;  // Możliwe wartości: string lub null
}
