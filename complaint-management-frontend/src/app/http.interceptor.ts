import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { AuthService } from './auth.service';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<any> {
    // Get the token from the AuthService
    const token = this.authService.getToken();

    // If the token exists, add it to the request header
    if (token) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(req).pipe(
      // Handle any errors (like 401 Unauthorized) if the token is expired or invalid
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // If the error status is 401 (Unauthorized), you can add logic to handle the expired token
          // e.g., redirect to login page, or refresh the token if using refresh tokens
          console.error('Unauthorized access - possible token expiry');
          // Optionally: Redirect to login page
          // this.authService.logout();
        }
        return throwError(error);
      })
    );
  }
}
