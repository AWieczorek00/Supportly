import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map, Observable} from 'rxjs';
import {Login} from '../Login';

@Injectable({
  providedIn: 'root'
})
export class HttpLoginService {

  constructor(private client: HttpClient) {
  }

  login(userDTO: Partial<Login>): Observable<{ token: string, expiresIn: number }> {
    console.log("leci rządanie")
    return this.client.post<{ token: string, expiresIn: number }>('http://localhost:8080/auth/login', userDTO).pipe(map((res:any) => {
      return res
    }));
  }
}
