import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';
import { BookingService } from '../booking.service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-passenger-profile',
  standalone: true,
  templateUrl: './passenger-profile.component.html',
  styleUrls: ['./passenger-profile.component.css'],
  imports: [CommonModule, FormsModule]
})
export class PassengerProfileComponent implements OnInit {
  profile: any;
  errorMessage: string | null = null;
  bookings: any[] = [];

  source: string = '';
  destination: string = '';
  fare: number | null = null;

  constructor(private authService: AuthService, private bookingService: BookingService, private router: Router) {}

  ngOnInit() {
    this.authService.getCurrentProfile().subscribe({
      next: (data) => {
        this.profile = data;
        this.bookings = data.bookings || [];
      },
      error: (err) => {
        if (err.status === 401) {
          this.errorMessage = 'Unauthorized access. Please log in.';
        } else {
          this.errorMessage = 'Failed to load profile. Please try again later.';
        }
        console.error('Error fetching profile', err);
      },
    });
  }

  calculateFare() {
    if (this.source && this.destination) {
      this.bookingService.calculateFare(this.source, this.destination).subscribe({
        next: (fare) => this.fare = fare,
        error: (err) => {
          console.error('Error calculating fare', err);
          this.fare = null;
        }
      });
    }
  }

  onBook() {
    if (this.source && this.destination && this.fare !== null) {
      const booking = {
        source: this.source,
        destination: this.destination,
        bookingTime: new Date(),
        fare: this.fare // Use the correctly calculated fare
      };

      this.bookingService.createBooking(booking).subscribe({
        next: (data) => {
          console.log('Booking created:', data);
          this.bookings.push(data); // Update bookings table
          // Clear form fields after successful booking
          this.source = '';
          this.destination = '';
          this.fare = null;
        },
        error: (err) => {
          console.error('Error creating booking', err);
        }
      });
    }
  }

  logout() {
    this.authService.logout(); // Call the logout function from AuthService
    this.router.navigate(['/login']); // Redirect to login page after logout
  }
}







