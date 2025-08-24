import {Component} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatCard, MatCardContent, MatCardModule, MatCardTitle} from '@angular/material/card';
import {MatFormField, MatFormFieldModule} from '@angular/material/form-field';
import {MatInput, MatInputModule} from '@angular/material/input';
import {MatButton, MatButtonModule} from '@angular/material/button';
import {EmployeeCriteria} from '../employee/EmployeeCriteria';
import {Login} from './Login';
import {HttpLoginService} from './service/http-login.service';
import {Router, RouterLink} from '@angular/router';


@Component({
  selector: 'app-login',
  imports: [
    MatCard,
    MatCardTitle,
    MatCardContent,
    ReactiveFormsModule,
    MatFormField,
    MatInput,
    MatButton,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatCardModule

  ],
  templateUrl: './login.component.html',
  standalone: true,
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  constructor(private http:HttpLoginService,private router: Router) {
  }

  loginForm = new FormGroup({
    email: new FormControl(''),
    password: new FormControl(''),
  });

  submit() {
    // if (this.form.valid) {
    //   this.submitEM.emit(this.form.value);
    // }
  }

  onSumitedLogined(loginForm: FormGroup) {
    const formValue = this.loginForm.value;

    const login: Login = {
      email: formValue.email ?? null,
      password: formValue.password ?? null,
    };

    this.http.login(login).subscribe({
      next: res => {
        console.log("Zalogowano!");
        localStorage.setItem('token',res.token)
        this.router.navigate([''
        ])
      },
      error: err => {
        console.error("Błąd logowania:");
      }
    });


  }
}
