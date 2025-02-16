import { Routes } from '@angular/router';
import {LoginComponent} from './login/login.component';
import {AppComponent} from './app.component';

export const routes: Routes = [
  {
    path: '',
    // canActivate: [AuthenticationGuard],
    children: [
      { path: 'dsada', component:  AppComponent },
      { path: '', component: LoginComponent },
      // { path: 'orders', loadChildren: () => OrderListPageModule },
      // { path: 'new-order', loadChildren: () => NewOrderPageModule },
      // { path: 'task', loadChildren: () => AddTaskPageModule },
      // { path: 'order/:id', loadChildren: () => OrderDetailsPageModule },
      // { path: 'employee', loadChildren: () => EmployeePageModule },

      { path: '**', redirectTo: '' },
    ],
  },
];
