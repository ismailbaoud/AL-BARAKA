import { Register } from './features/authentication/register/register';
import { Routes } from '@angular/router';
import { App } from './app';
import { Login } from './features/authentication/login/login';
import { AdminDashboard } from './features/dashboard/admin-dashboard/admin-dashboard';
import { ClientDashboard } from './features/dashboard/client-dashboard/client-dashboard';
import { AgentDashboard } from './features/dashboard/agent-dashboard/agent-dashboard';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'dashboard/admin', component: AdminDashboard },
  { path: 'dashboard/client', component: ClientDashboard },
  { path: 'dashboard/agent', component: AgentDashboard },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];