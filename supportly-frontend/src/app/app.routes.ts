import { Routes } from '@angular/router';
import {LoginComponent} from './login/login.component';
import {AppComponent} from './app.component';
import {HomeComponent} from './home/home.component';
import {AgreementListComponent} from './agreement/agreement-list/agreement-list.component';
import {AgreementAddComponent} from './agreement/agreement-add/agreement-add.component';
import {OrderListComponent} from './order/order-list/order-list.component';
import {OrderAddComponent} from './order/order-add/order-add.component';
import {EmployeeListComponent} from './employee/employee-list/employee-list.component';
import {EmployeeAddComponent} from './employee/employee-add/employee-add.component';
import {TaskListComponent} from './task/task-list/task-list.component';
import {TaskAddComponent} from './task/task-add/task-add.component';
import {authenticationGuard} from './auth/authentication.guard';

export const routes: Routes = [
  {
    path: '',
    children: [
      { path: '', component: HomeComponent, canActivate: [authenticationGuard] },
      { path: 'dsada', component: AppComponent, canActivate: [authenticationGuard] },
      { path: 'login', component: LoginComponent }, // NIE chronimy logowania
      { path: 'agreement/list', component: AgreementListComponent, canActivate: [authenticationGuard] },
      { path: 'agreement/add', component: AgreementAddComponent, canActivate: [authenticationGuard] },
      { path: 'order/list', component: OrderListComponent, canActivate: [authenticationGuard] },
      { path: 'order/add', component: OrderAddComponent, canActivate: [authenticationGuard] },
      { path: 'employee/list', component: EmployeeListComponent},
      { path: 'employee/add', component: EmployeeAddComponent, canActivate: [authenticationGuard] },
      { path: 'task/list', component: TaskListComponent, canActivate: [authenticationGuard] },
      { path: 'task/add', component: TaskAddComponent, canActivate: [authenticationGuard] },
      { path: '**', redirectTo: '' },
    ],
  },
];
