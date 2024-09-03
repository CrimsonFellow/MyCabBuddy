import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [RouterModule, CommonModule],
})
export class AppComponent {
  isLoggedIn = false;

  constructor(private router: Router) {}

  ngOnInit() {
    if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
      const currentUser = localStorage.getItem('currentUser');
      this.isLoggedIn = !!currentUser;
    }
  }

  logout() {
    if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
      localStorage.removeItem('currentUser');
    }
    this.isLoggedIn = false;
    this.router.navigate(['/login']);
  }
}




