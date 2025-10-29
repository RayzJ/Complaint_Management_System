import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth.service';  // Import AuthService
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-create-ticket',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './create-ticket.component.html',
  styleUrls: ['./create-ticket.component.css']
})
export class CreateTicketComponent {
  ticket = {
    id: null,  // Add the 'id' property here
    title: '',
    description: '',
    priority: '',
    status: 'open',
    customerId: ''  // Update the type to string (or number) instead of null
  };
  loading: boolean = false;
  error: string = '';
  supportAgents: any[] = [];  // To store the list of support agents
  isAssigning: boolean = false; // To control the display of the assign dialog
  isAdmin: boolean = false;  // Variable to check if the user is an admin



  constructor(private http: HttpClient, private authService: AuthService, private router: Router) {
    // Check if the user is an admin (this check should be done using JWT or roles)
    const userRole = this.authService.getUserRole();  // Assuming your AuthService has a method to get the user role
    if (userRole === 'ADMIN') {
      this.isAdmin = true; // If the user is an admin, set isAdmin to true
    }
  }
  onSubmit() {
    const token = this.authService.getToken(); // Get the JWT token from AuthService

    if (!token) {
      this.error = 'You are not authorized. Please log in.';
      return;
    }

    // Retrieve the logged-in user's ID from AuthService
    const loggedInUserId = this.authService.getUserId();
    if (!loggedInUserId) {
      this.error = 'Customer ID is missing. Please log in.';
      return;
    }

    // Set the customerId in the ticket object
    this.ticket.customerId = loggedInUserId; // Now the value can be a string or number

    // Create the headers with the token
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'  // Set content type to JSON
    });

    // Send POST request to create ticket
    this.loading = true;
    this.http.post('http://localhost:8080/api/tickets', this.ticket, { headers })
      .subscribe(
        (response: any) => {
          // Assign the id of the created ticket from the response
          this.ticket.id = response.id;  // Ensure you have 'id' in the response body

          // Handle success, redirect to the dashboard or another page
          this.router.navigate(['/dashboard']);
        },
        (error) => {
          // Handle error
          this.error = 'Failed to create ticket. Please try again.';
          console.error(error);
        }
      );
  }

  // Fetch the list of support agents when admin clicks on the "Assign" button
  openAssignDialog(ticketId: number) {
    // Check if ticketId is not null before proceeding
    if (!ticketId) {
      this.error = 'Ticket ID is missing. Please create a ticket first.';
      return;
    }

    this.isAssigning = true;  // Show the assignment dialog
    this.http.get<any[]>(`http://localhost:8080/api/tickets/${ticketId}/assign-agents`).subscribe(
      (agents) => {
        this.supportAgents = agents;  // Store the list of agents
      },
      (error) => {
        console.error('Failed to load support agents', error);
        this.error = 'Failed to load support agents.';
      }
    );
  }

  // Assign the ticket to a selected agent
  assignTicketToAgent(agentId: number) {
    const token = this.authService.getToken(); // Get the JWT token from AuthService

    if (!token) {
      this.error = 'You are not authorized. Please log in.';
      return;
    }

    // Create the headers with the token
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'  // Set content type to JSON
    });

    // Send POST request to assign the agent to the ticket
    this.http.post(`http://localhost:8080/api/tickets/assign/${this.ticket.id}`, { agentId }, { headers })
      .subscribe(
        (response) => {
          console.log('Ticket assigned successfully', response);
          this.router.navigate(['/dashboard']); // Redirect or close the dialog
        },
        (error) => {
          this.error = 'Failed to assign ticket. Please try again.';
          console.error(error);
        }
      );
  }
}
