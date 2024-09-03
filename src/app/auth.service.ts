import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/passenger-profiles';

  constructor(private http: HttpClient) {}

  private isLocalStorageAvailable(): boolean {
    try {
      const testKey = '__test__';
      localStorage.setItem(testKey, testKey);
      localStorage.removeItem(testKey);
      return true;
    } catch (e) {
      return false;
    }
  }

  login(username: string, password: string): Observable<boolean> {
    const headers = new HttpHeaders({
      Authorization: 'Basic ' + btoa(`${username}:${password}`),
      'Content-Type': 'application/json',
    });

    return this.http.post(`${this.baseUrl}/login`, {}, { headers, observe: 'response' }).pipe(
      map((response) => {
        if (response.status === 200 && this.isLocalStorageAvailable()) {
          localStorage.setItem('authToken', 'Basic ' + btoa(`${username}:${password}`));
          return true;
        }
        return false;
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('Login error', error);
        return throwError(() => new Error('Login failed due to network or server issue.'));
      })
    );
  }

  getCurrentProfile(): Observable<any> {
    const authToken = this.isLocalStorageAvailable() ? localStorage.getItem('authToken') : null;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: authToken || '',
    });

    return this.http.get(`${this.baseUrl}/current`, { headers }).pipe(
      map((response) => response),
      catchError((error: HttpErrorResponse) => {
        console.error('Error fetching current profile', error);
        return throwError(() => new Error('Failed to fetch current profile.'));
      })
    );
  }

  register(name: string, email: string, username: string, password: string, phone: string): Observable<any> {
    const body = { name, email, username, password, phone };
    
    console.log('Sending registration data:', body);
    
    return this.http.post(`${this.baseUrl}/register`, body, { observe: 'response' }).pipe(
      map((response: any) => {
        console.log('Full response:', response);

        if (response.status === 200 && response.body?.success) {
          return response.body;
        } else {
          throw new Error(response.body?.error || 'Unknown auth error occurred');
        }
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('Registration auth error details:', {
          status: error.status,
          statusText: error.statusText,
          url: error.url,
          message: error.message,
          error: error.error,
        });

        return throwError(() => new Error('Registration auth failed. Please try again.'));
      })
    );
  }

  // New logout method
  logout(): void {
    if (this.isLocalStorageAvailable()) {
      localStorage.removeItem('authToken');
    }
    console.log('User logged out successfully');
  }
}











