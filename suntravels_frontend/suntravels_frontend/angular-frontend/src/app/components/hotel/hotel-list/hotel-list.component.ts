import { Component, OnInit } from '@angular/core';
import { HotelService } from '../../service/hotel.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http'; 
import { RouterOutlet } from '@angular/router';
import { Hotel } from '../../model/hotel.model';
import { Router } from '@angular/router';


@Component({
  selector: 'app-hotel-list',
  standalone: true,
 imports: [ RouterModule, CommonModule],
  templateUrl: './hotel-list.component.html',
  styleUrls: ['./hotel-list.component.css']
})
export class HotelListComponent implements OnInit {
  hotels: Hotel[] = [];
  errorMessage: string = '';

  constructor(private hotelService: HotelService, private router: Router) {}

  ngOnInit(): void {
    this.loadHotels();
  }

  loadHotels() {
    this.hotelService.getAllHotels().subscribe({
      next: data => this.hotels = data,
      error: err => this.errorMessage = 'Error loading hotels'
    });
  }

  deleteHotel(id: number) {
    if (confirm('Are you sure you want to delete this hotel?')) {
      this.hotelService.deleteHotel(id).subscribe(() => {
        this.hotels = this.hotels.filter(h => h.id !== id);
      });
    }
  }

  navigateToAddContract(hotelId: number, hotelName: string) {
    this.router.navigate(['/contracts/new'], { queryParams: { hotelId, hotelName } });
  }
  
  navigateToSearch() {
    this.router.navigate(['/search']);
  }
  
 
}
