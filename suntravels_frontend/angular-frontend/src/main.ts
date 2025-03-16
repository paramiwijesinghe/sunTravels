import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withFetch  } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes'; 


bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(withFetch()), // Enable fetch globally
    provideRouter(routes) // ✅ Provides routing services (including ActivatedRoute)
  ]
}).catch(err => console.error(err));
