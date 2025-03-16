import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HotelFormComponent } from './components/hotel/hotel-form/hotel-form.component';
import { HotelListComponent } from './components/hotel/hotel-list/hotel-list.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'angular-frontend';
}
