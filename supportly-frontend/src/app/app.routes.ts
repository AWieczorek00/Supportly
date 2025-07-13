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

export const routes: Routes = [
  {
    path: '',
    // canActivate: [AuthenticationGuard],
    children: [
      { path: '', component: HomeComponent },
      { path: 'dsada', component:  AppComponent },
      { path: 'login', component: LoginComponent },
      { path: 'agreement/list', component: AgreementListComponent },
      { path: 'agreement/add', component: AgreementAddComponent },
      { path: 'order/list', component: OrderListComponent },
      { path: 'order/add', component:  OrderAddComponent },
      { path: 'employee/list', component:  EmployeeListComponent },
      { path: 'employee/add', component:  EmployeeAddComponent },
      { path: 'task/list', component:  TaskListComponent },
      { path: 'task/add', component:  TaskAddComponent },
      { path: '**', redirectTo: '' },
    ],
  },
];
