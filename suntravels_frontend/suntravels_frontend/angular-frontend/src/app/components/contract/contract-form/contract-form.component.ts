import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ContractService } from '../../service/contract.service';
import { Contract } from '../../model/contract.model';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RoomType } from '../../model/room-type.model';
import { RoomTypeService } from '../../service/room-type.service';

@Component({
  selector: 'app-contract-form',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './contract-form.component.html',
  styleUrls: ['./contract-form.component.css'],
})
export class ContractFormComponent implements OnInit {
  contract: Contract = {
    hotelId: 0,
    hotelName: '',
    startDate: '',
    endDate: '',
    markupPercentage: 0,
    roomTypes: [] // Array to store room types
  };

  newRoomType: RoomType = {
    id: undefined,  // Add id field
    name: '',
    pricePerPerson: 0,
    numberOfRooms: 0,
    maxAdults: 0
  };

  isEditing: boolean = false;
  editingIndex: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private contractService: ContractService,
    private router: Router,
    private roomTypeService: RoomTypeService, 
 
  ) {}

  loadRoomTypes(contractId: number) {
    this.roomTypeService.getRoomTypesByContractId(contractId).subscribe({
      next: (data) => {
        this.contract.roomTypes = data; // Load room types into contract object
      },
      error: (err) => {
        console.error("Error loading room types", err);
      }
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const hotelId = params['hotelId'];
      const hotelName = params['hotelName'];
      
      if (hotelId && hotelName) {
        this.contract.hotelId = +hotelId;
        this.contract.hotelName = hotelName;
        this.loadRoomTypes(this.contract.hotelId); // Load existing room types
      }
      
      const contractId = params['id']; // If editing an existing contract
      if (contractId) {
        this.loadContract(+contractId);
      }
    });
  }

  loadContract(id: number) {
    this.contractService.getContractById(id).subscribe((data) => {
      this.contract = data;
      console.log('Loaded contract with room types:', this.contract);
    });
  }

  addRoomType() {
    if (this.isEditing && this.editingIndex !== null) {
      // Update existing room type
      this.contract.roomTypes[this.editingIndex] = { ...this.newRoomType };
      this.isEditing = false;
      this.editingIndex = null;
    } else {
      // Add new room type
      this.contract.roomTypes.push({ ...this.newRoomType });
    }
    
    // Reset the form
    this.resetRoomTypeForm();
  }

  editRoomType(index: number) {
    this.newRoomType = { ...this.contract.roomTypes[index] };
    this.isEditing = true;
    this.editingIndex = index;
  }

  removeRoomType(index: number) {
    this.contract.roomTypes.splice(index, 1);
  }

  resetRoomTypeForm() {
    this.newRoomType = { 
      id: undefined,
      name: '', 
      pricePerPerson: 0, 
      numberOfRooms: 0, 
      maxAdults: 0 
    };
    this.isEditing = false;
    this.editingIndex = null;
  }

  // saveContract() {
  //   console.log('Attempting to save contract:', this.contract);
  
  //   if (this.contract?.id) {
  //     console.log("Updating existing contract with ID:", this.contract.id);
  //     this.contractService.updateContract(this.contract.id, this.contract).subscribe(() => {
  //       alert('Contract updated successfully!');
  //       this.router.navigate(['/contracts']);
  //     }, error => {
  //       console.error("Error updating contract:", error);
  //     });
  //   } else {
  //     console.log("Creating a new contract...");
  //     this.contractService.addContract(this.contract).subscribe(response => {
  //       console.log("Contract successfully saved:", response);
  //       alert('Contract added successfully!');
  //       this.router.navigate(['/contracts']);
  //     }, error => {
  //       console.error("Error saving contract:", error);
  //     });
  //   }
  // }
  saveContract() {
    console.log('Attempting to save contract:', this.contract);
  
    if (this.contract?.id) {
      console.log("Updating existing contract with ID:", this.contract.id);
      this.contractService.updateContract(this.contract.id, this.contract).subscribe(() => {
        alert('Contract updated successfully!');
        this.router.navigate(['/contracts']);
      }, error => {
        console.error("Error updating contract:", error);
      });
    } else {
      console.log("Creating a new contract...");
      this.contractService.addContract(this.contract).subscribe(response => {
        console.log("Contract successfully saved:", response);
        alert('Contract added successfully!');
        this.router.navigate(['/contracts']);
      }, error => {
        console.error("Error saving contract:", error);
      });
    }
  }
  
  
  
}