import { Component, OnInit } from '@angular/core';
import { RoomTypeService } from '../../service/room-type.service';
import { ActivatedRoute } from '@angular/router';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-room-type-list',
  imports: [RouterModule, CommonModule],
  templateUrl: './room-type-list.component.html',
  styleUrls: ['./room-type-list.component.css']
})
export class RoomTypeListComponent implements OnInit {
  contractId!: number;
  roomTypes: any[] = [];

  constructor(private roomTypeService: RoomTypeService,
     private route: ActivatedRoute,
     private router: Router,) {}

  ngOnInit(): void {
    this.contractId = Number(this.route.snapshot.paramMap.get('contractId'));
    if (this.contractId) {
      this.getRoomTypes();
    }
  }

  getRoomTypes() {
    this.roomTypeService.getRoomTypesByContractId(this.contractId).subscribe(
      (data) => this.roomTypes = data,
      (error) => console.error('Error fetching room types', error)
    );
  }

  deleteRoomType(roomTypeId: number) {
    if (confirm('Are you sure you want to delete this room type?')) {
      this.roomTypeService.deleteRoomType(roomTypeId).subscribe(() => {
        this.roomTypes = this.roomTypes.filter(rt => rt.id !== roomTypeId); // Remove from UI
      }, error => {
        console.error('Error deleting room type', error);
      });
    }
  }
}
