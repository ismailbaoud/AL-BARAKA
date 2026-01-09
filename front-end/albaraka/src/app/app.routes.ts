
import { Routes } from '@angular/router';
// import { DashboardComponent } from './dashboard/dashboard.component';
import { Login } from './features/auth/components/login/login';

export const routes: Routes = [
  { path: 'login', component: Login },
  // {
  //   path: 'dashboard',
  //   component: DashboardComponent,
  //   canMatch: [authGuard]   // هنا بنحمي الصفحة دي
  // },
  // { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  // { path: '**', redirectTo: '/dashboard' }
];