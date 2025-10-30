import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],  // No need to import any specific modules here
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) { }

  login() {
    this.authService.login(this.username, this.password).subscribe(
      (response: any) => {
        if (response && response.token) {
          // On success, save the user details and token
          const { username, fullName, roles, token, userId } = response;

          if (userId) { // Check if userId exists
            this.authService.setCurrentUser(username, fullName, roles, token, userId);

            // Navigate to the dashboard after successful login
            this.router.navigate(['/dashboard']);
          } else {
            console.error("User ID is missing in the response");
          }
        }
      },
      (error: any) => {
        this.errorMessage = 'Invalid credentials';
        console.error('Login failed:', error);
      }
    );
  }

}
