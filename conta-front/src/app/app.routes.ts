// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { RegisterComponent } from './pages/auth/register.component';


export const routes: Routes = [
  {path: '',loadComponent: () => import('./pages/client/catalog/catalog.component').then(m => m.CatalogComponent)},
  {path: 'client/catalog',loadComponent: () =>import('./pages/client/catalog/catalog.component').then(m => m.CatalogComponent)},
  { path: 'auth/register', component: RegisterComponent },
  { path: 'auth/login', loadComponent: () => import('./pages/auth/login.component').then(m => m.LoginComponent) },
  {path: 'client/pago',loadComponent: () => import('./pages/client/pago/pago.component').then(m => m.PagoComponent)}



];
