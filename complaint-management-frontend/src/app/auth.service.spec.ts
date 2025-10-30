import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth'; // Your backend API URL
  private currentUserSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(private http: HttpClient, private router: Router) {
    // Check if the user is already logged in (with a valid token) on page load
    const user = localStorage.getItem('user');
    if (user) {
      this.currentUserSubject.next(JSON.parse(user));
    }
  }

  login(username: string, password: string) {
    return this.http.post<any>(`${this.apiUrl}/login`, { username, password })
      .subscribe(response => {
        const user = response.user; // assuming the backend returns the user data
        const token = response.token; // assuming the backend returns the JWT token

        // Store the token and user data in localStorage (or sessionStorage)
        localStorage.setItem('user', JSON.stringify(user));
        localStorage.setItem('token', token);

        // Set the current user
        this.currentUserSubject.next(user);
        this.router.navigate(['/dashboard']);
      });
  }

  logout() {
    // Clear the user and token from localStorage and set the current user to null
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
    this.router.navigate(['/']);
  }

  getCurrentUser() {
    return this.currentUserSubject.value;
  }

  getToken() {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();  // Check if there's a valid token in localStorage
  }
}
