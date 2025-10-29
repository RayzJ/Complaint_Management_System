import { Component, OnInit } from '@angular/core';
import { NotificationService } from '../notification.service';
import { AuthService } from '../auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit {
  notifications: any[] = [];
  unreadCount: number = 0;

  constructor(
    private notificationService: NotificationService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.fetchNotifications();
  }

  // Fetch notifications from the backend
  fetchNotifications() {
    const userId = localStorage.getItem('userId');
    const token = this.authService.getToken();

    this.notificationService.getNotifications(Number(userId)).subscribe(
      (data) => {
        this.notifications = data;
        this.unreadCount = this.notifications.filter(n => !n.isRead).length;
      },
      (error) => {
        console.error('Error fetching notifications', error);
      }
    );
  }

  // Mark notification as read
  markAsRead(notificationId: number) {
    this.notificationService.markAsRead(notificationId).subscribe(
      () => {
        this.fetchNotifications();  // Refresh notifications
        this.unreadCount -= 1;  // Decrease unread count
      },
      (error) => {
        console.error('Error marking notification as read', error);
      }
    );
  }
}
