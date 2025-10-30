import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private apiUrl = 'http://localhost:8080/api/notifications';  // Replace with your API URL

  constructor(private http: HttpClient, private authService: AuthService) {}

  // Fetch notifications for the logged-in user
  getNotifications(userId: number): Observable<any[]> {
    const token = this.authService.getToken();
    if (!token) {
      return throwError('No authentication token');
    }
    
    return this.http.get<any[]>(`${this.apiUrl}/user/${userId}`, {
      headers: { Authorization: `Bearer ${token}` }
    }).pipe(
      catchError(error => {
        console.error('Backend notification API not available:', error);
        return throwError(error);
      })
    );
  }

  // Mark notification as read
  markAsRead(notificationId: number): Observable<any> {
    const token = this.authService.getToken();
    if (!token) {
      return throwError('No authentication token');
    }
    
    return this.http.post(`${this.apiUrl}/${notificationId}/read`, {}, {
      headers: { Authorization: `Bearer ${token}` }
    }).pipe(
      catchError(error => {
        console.error('Backend notification API not available:', error);
        return throwError(error);
      })
    );
  }

  // Send notification to a user
  sendNotification(notificationData: any): Observable<any> {
    const token = this.authService.getToken();
    return this.http.post(`${this.apiUrl}/send`, notificationData, {
      headers: { 
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
  }

  // Get unread count for a user
  getUnreadCount(userId: number): Observable<number> {
    const token = this.authService.getToken();
    return this.http.get<number>(`${this.apiUrl}/user/${userId}/unread-count`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }
}
