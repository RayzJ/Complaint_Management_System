import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

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
      this.currentUserSubject.next(JSON.parse(user));  // If user exists, set the current user
    }
  }

  // Login method: returns Observable, allowing the component to handle subscription
  login(username: string, password: string) {
    return this.http.post<any>(`${this.apiUrl}/login`, { username, password });
  }

  // Set the current user in BehaviorSubject and store it in localStorage
  setCurrentUser(username: string, fullName: string, roles: string[], token: string, userId: number) {
    if (!userId) {
      console.error("User ID is missing in the response");
      return;
    }

    localStorage.setItem('username', username);
    localStorage.setItem('token', token);  // Store JWT token
    localStorage.setItem('fullName', fullName);
    localStorage.setItem('roles', JSON.stringify(roles));
    localStorage.setItem('userId', userId.toString());  // Store user ID
    localStorage.setItem('role', roles[0]);  // Assuming the first role is the primary role

    this.currentUserSubject.next({ username, fullName, roles, userId, token });
  }

  // Decode the JWT token and extract the user's role
  decodeToken(token: string): any {
    return jwtDecode(token);
  }

  // Get the user's role from the JWT token
  getUserRole(): string {
    const token = this.getToken();
    if (!token) {
      return 'Unknown'; // Return a default value if no token is present
    }
    const decodedToken = this.decodeToken(token);
    return decodedToken ? decodedToken.role : 'Unknown'; // Assuming the JWT contains a 'role' field
  }

  // Get the user ID from the JWT token
  getUserId(): string | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }
    const decodedToken = this.decodeToken(token);
    return decodedToken ? decodedToken.userId?.toString() : null; // Return as string
  }

  // Get the current user from BehaviorSubject
  getCurrentUser() {
    return this.currentUserSubject.value;
  }

  // Get the JWT token from localStorage
  getToken() {
    return localStorage.getItem('token');
  }

  // Check if the user is authenticated based on the presence of a valid token
  isAuthenticated(): boolean {
    return !!this.getToken();  // Check if there's a valid token in localStorage
  }

  // Logout method to clear session and navigate to login
  logout() {
    // Clear the user and token from localStorage and set the current user to null
    localStorage.removeItem('username');
    localStorage.removeItem('token');
    localStorage.removeItem('fullName');
    localStorage.removeItem('roles');
    localStorage.removeItem('userId');
    localStorage.removeItem('role');
    
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }
}
