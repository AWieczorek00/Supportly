export abstract class BaseComponent {

  // Pobranie tokena z localStorage
  protected getToken(): string | null {
    return localStorage.getItem('token');
  }

  // Dekodowanie payload JWT
  protected decodeToken(token: string): any | null {
    try {
      const payloadBase64 = token.split('.')[1];
      const payloadJson = atob(payloadBase64.replace(/-/g, '+').replace(/_/g, '/'));
      return JSON.parse(payloadJson);
    } catch (e) {
      console.error('Błąd przy dekodowaniu tokena', e);
      return null;
    }
  }

  // Pobranie ID użytkownika
  protected getUserId(): number | null {
    const token = this.getToken();
    if (!token) return null;
    const payload = this.decodeToken(token);
    return payload?.id ?? null;
  }

  // Opcjonalnie: pobranie roli
  protected getRole(): string | null {
    const token = this.getToken();
    if (!token) return null;
    const payload = this.decodeToken(token);
    return payload?.role?.name ?? null;
  }
}
