import { Routes } from '@angular/router';
import { CreateTicketComponent } from './create-ticket/create-ticket.component';
import { NotificationComponent } from './notification/notification.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { LoginComponent } from './login/login.component';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'notifications', component: NotificationComponent },  // Route for notifications
  { path: 'create-ticket', component: CreateTicketComponent },  // Route for creating a new ticket
];
