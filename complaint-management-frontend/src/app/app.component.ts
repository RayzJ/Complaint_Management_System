import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { routes } from './app.routes';  // Import routes from app.routes.ts

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule],  // Ensure RouterModule is imported
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  username = 'customer_1';  // Example username, replace with actual logic
  title = 'Complaint Management System';
}

