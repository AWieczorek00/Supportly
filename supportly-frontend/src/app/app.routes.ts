import { Routes } from '@angular/router';
import {LoginComponent} from './login/login.component';
import {AppComponent} from './app.component';
import {HomeComponent} from './home/home.component';
import {AgreementListComponent} from './agreement/agreement-list/agreement-list.component';

export const routes: Routes = [
  {
    path: '',
    // canActivate: [AuthenticationGuard],
    children: [
      { path: '', component: HomeComponent },
      { path: 'dsada', component:  AppComponent },
      { path: 'login', component: LoginComponent },
      { path: 'agreement/list', component: AgreementListComponent },
      // { path: 'orders', loadChildren: () => OrderListPageModule },
      // { path: 'new-order', loadChildren: () => NewOrderPageModule },
      // { path: 'task', loadChildren: () => AddTaskPageModule },
      // { path: 'order/:id', loadChildren: () => OrderDetailsPageModule },
      // { path: 'employee', loadChildren: () => EmployeePageModule },

      { path: '**', redirectTo: '' },
    ],
  },
];
