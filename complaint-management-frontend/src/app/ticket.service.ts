import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  private apiUrl = environment.apiUrl + '/tickets'; // Replace with your API URL

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
