import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common'; 
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [FormsModule, CommonModule, RouterModule], // Add RouterModule here
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    console.log('Username:', this.username);
    console.log('Password:', this.password); 

    if (!this.username || !this.password) {
      this.errorMessage = 'Please enter both username and password.';
      return;
    }

    this.authService.login(this.username, this.password).subscribe({
      next: (isAuthenticated: boolean) => {
        console.log('Is Authenticated:', isAuthenticated); 
        if (isAuthenticated) {
          console.log('Navigation to passenger profile');
          this.router.navigate(['/passenger-profile']);
        } else {
          this.errorMessage = 'Login failed. Please check your credentials.';
        }
      },
      error: (err) => {
        console.error('Login error', err);
        this.errorMessage = 'An error occurred during login. Please try again.';
      },
    });
  }
}







