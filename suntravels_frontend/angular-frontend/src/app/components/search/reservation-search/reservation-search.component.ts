import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SearchService } from '../../service/search.service';
import { SearchRequest } from '../../model/search-request.model';
import { SearchResult } from '../../model/search-result.model';
import { AssignedRoom } from '../../model/assignedRoom.model';



@Component({
  selector: 'app-search',
  standalone: true,
  imports: [ RouterModule, CommonModule, FormsModule],
  templateUrl: './reservation-search.component.html',
  styleUrls: ['./reservation-search.component.css']
})
export class ReservationSearchComponent {
  searchResults: SearchResult[] = [];
  searchRequest: SearchRequest = {
    checkInDate: '',
    numberOfNights: 1,
    roomRequests: [{ numberOfAdults: 1 }]
  };
  isLoading = false;
  errorMessage: string | null = null;

  constructor(private searchService: SearchService) {}

  searchRooms() {
    this.isLoading = true;
    this.errorMessage = null;
  
    this.searchService.searchAvailableRooms(this.searchRequest).subscribe(
      (results) => {
        this.searchResults = results.map((hotel) => {
          let assignedRooms: AssignedRoom[] = [];
          let totalPrice = 0;
  
          // Allocate rooms based on requests
          this.searchRequest.roomRequests.forEach((request) => {
            let suitableRoom = hotel.availableRoomTypes.find(room => room.maxAdults >= request.numberOfAdults);
  
            if (suitableRoom) {
              assignedRooms.push({
                roomName: suitableRoom.name,
                maxAdults: suitableRoom.maxAdults,
                price: suitableRoom.totalPrice,
                assignedAdults: request.numberOfAdults
              });
  
              totalPrice += suitableRoom.totalPrice;
            }
          });
  
          return {
            hotelName: hotel.hotelName,
            availableRoomTypes: hotel.availableRoomTypes, // Keep for debugging if needed
            assignedRooms: assignedRooms, // New structured data
            totalPrice: totalPrice
          };
        });
  
        this.isLoading = false;
      },
      (error) => {
        this.errorMessage = 'Error fetching search results.';
        this.isLoading = false;
      }
    );
  }
  
  

  addRoomRequest() {
    this.searchRequest.roomRequests.push({ numberOfAdults: 1 });
  }

  removeRoomRequest(index: number) {
    this.searchRequest.roomRequests.splice(index, 1);
  }
}
