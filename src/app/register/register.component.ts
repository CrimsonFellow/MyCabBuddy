import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [FormsModule],
})
export class RegisterComponent {
  name: string = '';
  email: string = '';
  username: string = '';
  password: string = '';
  phone: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    console.log('Form submitted with values:', {
      name: this.name,
      email: this.email,
      username: this.username,
      password: this.password,
      phone: this.phone,
    });

    if (!this.name || !this.email || !this.username || !this.password || !this.phone) {
      alert('All fields are required');
      return;
    }

    console.log('Calling AuthService.register...'); // Log before calling the service

    this.authService.register(this.name, this.email, this.username, this.password, this.phone)
      .subscribe({
        next: (response: any) => {
          console.log('Registration successful:', response);  // Log on success
          if (response) {
            this.router.navigate(['/login']);
          } else {
            alert('Registration registration component failed');
          }
        },
        error: (error) => {
          console.error('Registration error:', error);
          alert('Registration registration component failed. Please try again.');
        }
      });
  }
}





