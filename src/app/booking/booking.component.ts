import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BookingService } from '../booking.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-booking',
  standalone: true,
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.css'],
  imports: [FormsModule, CommonModule],
})
export class BookingComponent implements OnInit {
  bookings: any[] = [];
  newBooking: any = {
    source: '',
    destination: '',
    bookingTime: '',
  };
  bookingId: string | null = null;

  constructor(
    private bookingService: BookingService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.bookingId = this.route.snapshot.paramMap.get('id');
    this.loadBookings();
  }

  loadBookings(): void {
    this.bookingService.getBookings().subscribe({
      next: (data: any[]) => {
        this.bookings = data;
      },
      error: (err) => {
        console.error('Error loading bookings', err);
        alert('An error occurred while loading bookings.');
      },
    });
  }

  createBooking(): void {
    if (this.isBookingValid()) {
      this.bookingService.createBooking(this.newBooking).subscribe({
        next: () => {
          this.loadBookings();
          this.newBooking = {
            source: '',
            destination: '',
            bookingTime: '',
          };
          alert('Booking created successfully!');
        },
        error: (err) => {
          console.error('Error creating booking', err);
          alert('An error occurred while creating the booking.');
        },
      });
    } else {
      alert('Please fill in all required fields.');
    }
  }

  isBookingValid(): boolean {
    return (
      this.newBooking.source.trim() !== '' &&
      this.newBooking.destination.trim() !== '' &&
      this.newBooking.bookingTime.trim() !== ''
    );
  }
}





