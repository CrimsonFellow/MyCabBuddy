import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component'; 
import { BookingComponent } from './booking/booking.component';
import { PassengerProfileComponent } from './passenger-profile/passenger-profile.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' }, // Default route
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent }, 
  { path: 'booking', component: BookingComponent },
  { path: 'passenger-profile', component: PassengerProfileComponent },
];


