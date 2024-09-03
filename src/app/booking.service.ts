import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  private baseUrl = 'http://localhost:8080/api/bookings';

  constructor(private http: HttpClient) {}

  calculateFare(source: string, destination: string): Observable<number> {
    const authToken = localStorage.getItem('authToken');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: authToken || '',
    });
    return this.http.get<number>(
      `${this.baseUrl}/calculate-fare?source=${encodeURIComponent(source)}&destination=${encodeURIComponent(destination)}`,
      { headers }
    );
  }

  createBooking(booking: any): Observable<any> {
    const authToken = localStorage.getItem('authToken');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: authToken || '',
    });
    return this.http.post(this.baseUrl, booking, { headers });
  }

  getBookings(): Observable<any[]> {
    const authToken = localStorage.getItem('authToken');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: authToken || '',
    });
    return this.http.get<any[]>(this.baseUrl, { headers });
  }
}

