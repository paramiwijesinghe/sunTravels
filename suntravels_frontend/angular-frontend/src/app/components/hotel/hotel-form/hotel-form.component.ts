import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators , ReactiveFormsModule } from '@angular/forms';
import { HotelService } from '../../service/hotel.service';
import { Hotel } from '../../model/hotel.model';

@Component({
  selector: 'app-hotel-form',
  standalone: true,
  imports: [ RouterModule, CommonModule, ReactiveFormsModule ],
  templateUrl: './hotel-form.component.html',
  styleUrls: ['./hotel-form.component.css']
})
export class HotelFormComponent implements OnInit {
  hotelForm: FormGroup;
  isEditMode = false;
  hotelId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private hotelService: HotelService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.hotelForm = this.fb.group({
      name: ['', Validators.required],
      location: ['', Validators.required],
      contactDetails: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.hotelId = +id;
        this.loadHotel(this.hotelId);
      }
    });
  }

  loadHotel(id: number) {
    this.hotelService.getHotelById(id).subscribe(hotel => {
      this.hotelForm.patchValue(hotel);
    });
  }

  onSubmit() {
    const hotelData = this.hotelForm.value as Hotel;

    if (this.isEditMode && this.hotelId !== null) {
      this.hotelService.updateHotel(this.hotelId, hotelData).subscribe(() => {
        this.router.navigate(['/hotels']);
      });
    } else {
      this.hotelService.createHotel(hotelData).subscribe(() => {
        this.router.navigate(['/hotels']);
      });
    }
  }
}
