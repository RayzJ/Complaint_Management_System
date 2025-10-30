import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  private apiUrl = 'http://localhost:8080/api/tickets'; // Replace with your API URL

  constructor(private http: HttpClient) {}

  // Create a new ticket
  createTicket(ticket: any): Observable<any> {
    return this.http.post(this.apiUrl, ticket);
  }

  // Fetch tickets from the API
  getTickets(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}
