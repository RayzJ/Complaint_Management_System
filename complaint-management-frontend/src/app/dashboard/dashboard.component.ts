import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { TicketService } from '../ticket.service'; 
import { NotificationService } from '../notification.service';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth.service';  // Make sure AuthService is available
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  tickets: any[] = [];
  notifications: any[] = [];
  unreadCount: number = 0;
  loading: boolean = false;
  error: string = '';
  searchQuery: string = '';
  sortOption: string = 'priority';
  role: string = '';
  userId: string = '';
  isAdmin: boolean = false;
  isSupporter: boolean = false;
  supportAgents: any[] = [];
  allUsers: any[] = [];
  selectedTicketId: number | null = null;
  showStatusDialog: boolean = false;
  showAssignDialog: boolean = false;
  showNotifications: boolean = false;
  newStatus: string = '';
  selectedUserId: string = '';
  newNotificationMessage: string = '';
  customerSearchQuery: string = '';
  filteredUsers: any[] = [];
  showUserDropdown: boolean = false;
  showCreateNotification: boolean = false;
  agentNames: { [key: number]: string } = {}; // Cache for agent names
  readNotifications: Set<number> = new Set(); // Track read notifications for sample data

  constructor(
    private ticketService: TicketService,
    private notificationService: NotificationService,
    private authService: AuthService,  // Make sure AuthService is available
    private router: Router,
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.role = localStorage.getItem('role') || 'CUSTOMER';
    this.userId = localStorage.getItem('userId') || '1';
    this.isAdmin = this.role === 'ADMIN' || this.role === 'Admin' || this.role === 'admin';
    this.isSupporter = this.role === 'SUPPORT_AGENT' || this.role === 'Support_Agent' || this.role === 'support_agent' || this.role === 'SUPPORT AGENT' || this.role === 'Support Agent' || this.role.toLowerCase().includes('support');
    
    console.log('Dashboard initialized:', { 
      role: this.role, 
      userId: this.userId, 
      isAdmin: this.isAdmin, 
      isSupporter: this.isSupporter,
      roleType: typeof this.role,
      roleCheck: this.role === 'ADMIN',
      supportCheck: this.role === 'SUPPORT_AGENT',
      supportSpaceCheck: this.role === 'SUPPORT AGENT'
    });
    
    this.fetchTickets();
    this.fetchNotifications();
    
    if (this.isAdmin || this.isSupporter) {
      this.fetchAllUsers();
    }
  }

  // Fetch tickets from the backend API
  fetchTickets() {
    const token = this.authService.getToken(); // Get token from AuthService

    if (!token) {
      this.error = 'You are not logged in.';
      this.loading = false;
      return;
    }

    const userId = localStorage.getItem('userId');  // Get logged-in userId from localStorage
    const role = localStorage.getItem('role');  // Get role from localStorage

    if (!userId || !role) {
      this.error = 'User data is missing';
      this.loading = false;
      return;
    }

    // Create the headers with the token
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    // Make the GET request to fetch tickets for the current user and role
    this.http.get<any[]>(`${environment.apiUrl}/tickets?role=${role}&userId=${userId}`, { headers })
      .subscribe(
        (data: any) => {
          this.tickets = data;
          this.loading = false;
        },
        (error: any) => {
          this.error = 'Error fetching tickets';
          this.loading = false;
          console.error(error);
        }
      );
  }

  // Method to navigate to the "Create Ticket" page
  createTicket() {
    this.router.navigate(['/create-ticket']);  // Navigate to the create ticket route
  }

  // Method to handle sorting (sorting by priority or status)
  sortTickets() {
    if (this.sortOption === 'priority') {
      this.tickets.sort((a, b) => a.priority.localeCompare(b.priority));
    } else if (this.sortOption === 'status') {
      this.tickets.sort((a, b) => a.status.localeCompare(b.status));
    }
  }

  // Method to handle user logout
  onLogout() {
    console.log('User logged out');
    // Implement your logout logic here (e.g., clear session or token)
    this.authService.logout();  // Make sure logout() clears session storage and token
    this.router.navigate(['/']);  // Redirect to the login page
  }

  // Method to get the username
  getUsername() {
    return localStorage.getItem('username') || 'User';
  }

  // Toggle notification panel and auto-mark as read
  toggleNotifications() {
    this.showNotifications = !this.showNotifications;
    if (this.showNotifications) {
      this.fetchNotifications();
      // Auto-mark unread notifications as read after 2 seconds
      setTimeout(() => {
        this.notifications.forEach(notification => {
          if (!notification.isRead) {
            this.markAsRead(notification.id);
          }
        });
      }, 2000);
    }
  }

  // Close notification panel
  closeNotifications() {
    this.showNotifications = false;
  }

  // Fetch notifications from backend with fallback
  fetchNotifications() {
    // Admin and support agents don't receive notifications, only customers do
    if (this.isAdmin || this.isSupporter) {
      console.log('Admin/Support agent - no notifications', { role: this.role, isAdmin: this.isAdmin, isSupporter: this.isSupporter });
      this.notifications = [];
      this.unreadCount = 0;
      this.cdr.detectChanges();
      return;
    }
    
    const userId = Number(this.userId);
    console.log('Fetching notifications for customer userId:', userId);
    
    this.notificationService.getNotifications(userId).subscribe(
      (data) => {
        console.log('Raw backend data:', data);
        
        // Map backend data to frontend format
        this.notifications = (data || []).map(notification => {
          const mapped = {
            ...notification,
            message: notification.body || notification.message || 'No content',
            isRead: notification.isRead === 1 || notification.isRead === true || this.readNotifications.has(notification.id)
          };
          return mapped;
        });
        
        this.unreadCount = this.notifications.filter(n => !n.isRead).length;
        this.cdr.detectChanges();
      },
      (error) => {
        console.warn('Backend not available, using mock data:', error);
        this.notifications = this.getMockNotifications();
        this.unreadCount = this.notifications.filter(n => !n.isRead).length;
      }
    );
  }

  // Auto-mark notifications as read when viewed
  markAsRead(notificationId: number) {
    const notification = this.notifications.find(n => n.id === notificationId);
    if (notification && !notification.isRead) {
      notification.isRead = true;
      this.readNotifications.add(notificationId);
      this.unreadCount = this.notifications.filter(n => !n.isRead).length;
      this.cdr.detectChanges();
      
      // Sync with backend silently
      this.notificationService.markAsRead(notificationId).subscribe();
    }
  }

  // Fetch all users from backend with fallback
  fetchAllUsers() {
    const token = this.authService.getToken();
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    
    this.http.get<any[]>(environment.apiUrl + '/users', { headers })
      .subscribe(
        (users) => {
          this.allUsers = users || [];
          console.log('Users loaded from backend:', this.allUsers);
        },
        (error) => {
          console.warn('Backend not available, using mock users:', error);
          this.allUsers = this.getMockUsers();
        }
      );
  }

  // Send notification to backend with fallback
  sendNotification() {
    if (!this.selectedUserId || !this.newNotificationMessage?.trim()) return;
    
    const token = this.authService.getToken();
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' });
    
    const notificationData = {
      userId: Number(this.selectedUserId),
      message: this.newNotificationMessage.trim(),
      senderId: Number(this.userId)
    };
    
    this.http.post(environment.apiUrl + '/notifications/send', notificationData, { headers })
      .subscribe(
        (response) => {
          console.log('Notification sent successfully:', response);
          this.clearNotificationForm();
          alert('Notification sent successfully!');
        },
        (error) => {
          console.warn('Backend not available, simulating send:', error);
          // Simulate successful send for demo
          const newNotification = {
            id: Date.now(),
            message: this.newNotificationMessage,
            isRead: false,
            createdAt: new Date().toISOString(),
            senderId: Number(this.userId)
          };
          this.notifications.unshift(newNotification);
          this.unreadCount = this.notifications.filter(n => !n.isRead).length;
          this.clearNotificationForm();
          alert('Notification sent successfully! (Demo mode - backend not connected)');
        }
      );
  }

  // Open status change dialog
  openStatusDialog(ticketId: number) {
    this.selectedTicketId = ticketId;
    this.showStatusDialog = true;
  }

  // Open assign dialog and fetch support agents
  openAssignDialog(ticketId: number) {
    this.selectedTicketId = ticketId;
    this.showAssignDialog = true;
    this.fetchSupportAgents();
  }

  // Fetch support agents for assignment
  fetchSupportAgents() {
    const token = this.authService.getToken();
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    
    this.http.get<any[]>(environment.apiUrl + '/notifications/users/support-agents', { headers })
      .subscribe(
        (agents) => {
          this.supportAgents = agents || [];
          console.log('Support agents loaded:', this.supportAgents);
        },
        (error) => {
          console.error('Failed to load support agents, using fallback:', error);
          this.supportAgents = [
            { id: 2, fullName: 'John Smith', username: 'support1' },
            { id: 4, fullName: 'Mike Wilson', username: 'support2' }
          ];
        }
      );
  }

  // Change ticket status
  changeStatus() {
    if (!this.selectedTicketId || !this.newStatus) return;
    
    const token = this.authService.getToken();
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' });
    
    this.http.put(`${environment.apiUrl}/tickets/${this.selectedTicketId}/status`, 
      { status: this.newStatus }, { headers })
      .subscribe(
        (response) => {
          console.log('Status updated successfully:', response);
          alert('Status updated successfully!');
          this.fetchTickets();
          this.closeStatusDialog();
        },
        (error) => {
          console.error('Failed to update status, using demo mode:', error);
          alert('Status updated successfully! (Demo mode)');
          this.closeStatusDialog();
        }
      );
  }

  // Assign ticket to support agent
  assignTicket(agentId: number) {
    if (!this.selectedTicketId) return;
    
    const token = this.authService.getToken();
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' });
    
    this.http.post(`${environment.apiUrl}/tickets/assign/${this.selectedTicketId}`, 
      { agentId }, { headers })
      .subscribe(
        (response) => {
          console.log('Ticket assigned successfully:', response);
          alert('Ticket assigned successfully!');
          this.fetchTickets();
          this.closeAssignDialog();
        },
        (error) => {
          console.error('Failed to assign ticket, using demo mode:', error);
          alert('Ticket assigned successfully! (Demo mode)');
          this.closeAssignDialog();
        }
      );
  }

  // Close dialogs
  closeStatusDialog() {
    this.showStatusDialog = false;
    this.selectedTicketId = null;
    this.newStatus = '';
  }

  closeAssignDialog() {
    this.showAssignDialog = false;
    this.selectedTicketId = null;
    this.supportAgents = [];
  }

  // Helper method to get assigned agent name
  getAssignedAgentName(ticket: any): string {
    if (!ticket.assigneeId) return 'Unassigned';
    return `Agent ID: ${ticket.assigneeId}`;
  }

  // Helper method to get customer name
  getCustomerName(ticket: any): string {
    if (ticket.customerName) return `${ticket.customerName} (ID: ${ticket.customerId})`;
    if (ticket.customerId) return `Customer ID: ${ticket.customerId}`;
    return 'Unknown Customer';
  }

  // Mock data methods for demo purposes
  getMockNotifications(): any[] {
    // Only customers get mock notifications
    if (this.isAdmin || this.isSupporter) {
      return [];
    }
    
    return [
      {
        id: 1,
        title: 'Welcome',
        message: 'Welcome to the complaint management system!',
        isRead: false,
        createdAt: new Date().toISOString(),
        senderName: 'Admin'
      },
      {
        id: 2,
        title: 'System Update',
        message: 'System maintenance completed successfully.',
        isRead: true,
        createdAt: new Date(Date.now() - 3600000).toISOString(),
        senderName: 'Support Agent'
      }
    ];
  }

  getMockUsers(): any[] {
    return [
      { id: 1, fullName: 'Admin User', role: 'ADMIN', username: 'admin1' },
      { id: 2, fullName: 'John Smith (Support)', role: 'SUPPORT_AGENT', username: 'support1' },
      { id: 3, fullName: 'Jane Doe (Customer)', role: 'CUSTOMER', username: 'customer1' },
      { id: 4, fullName: 'Mike Wilson (Support)', role: 'SUPPORT_AGENT', username: 'support2' },
      { id: 5, fullName: 'Sarah Johnson (Customer)', role: 'CUSTOMER', username: 'customer2' }
    ];
  }

  // Clear notification form
  clearNotificationForm() {
    this.selectedUserId = '';
    this.newNotificationMessage = '';
    this.customerSearchQuery = '';
    this.filteredUsers = [];
    this.showUserDropdown = false;
  }

  // Track by function for notifications
  trackByNotificationId(index: number, notification: any): number {
    return notification.id;
  }

  // Search customers by name or ID
  searchCustomers() {
    if (!this.customerSearchQuery.trim()) {
      this.filteredUsers = [];
      this.showUserDropdown = false;
      return;
    }

    const query = this.customerSearchQuery.toLowerCase().trim();
    this.filteredUsers = this.allUsers.filter(user => 
      user.fullName.toLowerCase().includes(query) ||
      user.username.toLowerCase().includes(query) ||
      user.id.toString().includes(query)
    );
    this.showUserDropdown = this.filteredUsers.length > 0;
  }

  // Select user from search results
  selectUser(user: any) {
    this.selectedUserId = user.id.toString();
    this.customerSearchQuery = `${user.fullName} (${user.role})`;
    this.showUserDropdown = false;
  }

  // Get selected user name for display
  getSelectedUserName(): string {
    if (!this.selectedUserId) return '';
    const user = this.allUsers.find(u => u.id.toString() === this.selectedUserId);
    return user ? `${user.fullName} (${user.role})` : '';
  }
}
