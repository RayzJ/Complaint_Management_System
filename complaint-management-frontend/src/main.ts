import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { provideHttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';  // Import HTTP_INTERCEPTORS
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';
import { LoginComponent } from './app/login/login.component';
import { DashboardComponent } from './app/dashboard/dashboard.component';
import { JwtInterceptor } from './app/http.interceptor';

const appConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),  // Provide the HttpClient service
    {
      provide: HTTP_INTERCEPTORS,  // Add HTTP interceptor for all requests
      useClass: JwtInterceptor,
      multi: true  // Allow multiple interceptors to be added
    }
  ]
};

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
