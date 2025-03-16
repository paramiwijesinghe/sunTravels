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
    id: undefined,
    name: '',
    pricePerPerson: 0,
    numberOfRooms: 0,
    maxAdults: 0,
    contractId: undefined // Added contractId property
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
      const contractId = params['id']; // If editing an existing contract
      
      if (hotelId && hotelName) {
        this.contract.hotelId = +hotelId;
        this.contract.hotelName = hotelName;
      }
      
      if (contractId) {
        // Load the full contract including its room types
        this.loadContract(+contractId);
      }
    });
    
    // Also check for route parameters (not just query params)
    this.route.params.subscribe(params => {
      const contractId = params['id'] || params['contractId'];
      if (contractId) {
        this.loadContract(+contractId);
      }
    });
  }

  loadContract(id: number) {
    
    this.contractService.getContractById(id).subscribe({
      next: (data) => {
        // Format dates for date input
        if (data.startDate) {
          data.startDate = this.formatDateForInput(data.startDate);
        }
        if (data.endDate) {
          data.endDate = this.formatDateForInput(data.endDate);
        }
        
        this.contract = data;
        console.log('Loaded contract:', this.contract);
      //   this.contract = { ...data };  // Ensure object assignment
      // this.contract.id = id;  // Explicitly set contract ID if missing

        
        // Load associated room types
        this.loadRoomTypes(id);
      },
      error: (err) => {
        console.error("Error loading contract", err);
      }
    });
  }
  

  // Helper method to format dates for input elements
  formatDateForInput(date: any): string {
    if (typeof date === 'string') {
      // If it's already a string, check if it's in ISO format
      if (date.includes('T')) {
        return date.split('T')[0];
      }
      return date;
    }
    // If it's a Date object or something else, convert to string
    const d = new Date(date);
    return d.toISOString().split('T')[0];
  }

  addRoomType() {
    // Make sure the room type has the contract ID
    this.newRoomType.contractId = this.contract.id;
    
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
      maxAdults: 0,
      contractId: undefined
    };
    this.isEditing = false;
    this.editingIndex = null;
  }

  saveContract() {
    console.log('Attempting to save contract:', this.contract);
  
    if (this.contract?.id) {
      console.log("Updating existing contract with ID:", this.contract.id);
      this.contractService.updateContract(this.contract.id, this.contract).subscribe({
        next: (response) => {
          console.log("Contract updated successfully:", response);
          if (this.contract.id !== undefined) {
            this.saveRoomTypes(this.contract.id); // Ensure contract.id is a number
          }
        },
        error: (error) => {
          console.error("Error updating contract:", error);
        }
      });
    } else {
      console.log("Creating a new contract...");
      this.contractService.addContract(this.contract).subscribe({
        next: (response) => {
          console.log("Contract successfully saved:", response);
          if (response.id !== undefined) {
            this.saveRoomTypes(response.id); // Ensure response.id is defined before passing it
          }
        },
        error: (error) => {
          console.error("Error saving contract:", error);
        }
      });
    }
  }
  

  saveRoomTypes(contractId: number) {
    console.log(`Saving ${this.contract.roomTypes.length} room types for contract ${contractId}`);
    
    // Process each room type
    let completedCount = 0;
    let errorCount = 0;
    
    if (this.contract.roomTypes.length === 0) {
      alert('Contract saved successfully!');
      this.router.navigate(['/contracts']);
      return;
    }
    
    this.contract.roomTypes.forEach((roomType, index) => {
      // Ensure contractId is set
      roomType.contractId = contractId;
      
      if (roomType.id) {
        // Update existing room type
        this.roomTypeService.updateRoomType(roomType.id, roomType).subscribe({
          next: () => {
            completedCount++;
            this.checkCompletion(completedCount, errorCount);
          },
          error: (err) => {
            console.error(`Error updating room type ${index}:`, err);
            errorCount++;
            this.checkCompletion(completedCount, errorCount);
          }
        });
      } else {
        // Create new room type
        this.roomTypeService.createRoomType(roomType).subscribe({
          next: () => {
            completedCount++;
            this.checkCompletion(completedCount, errorCount);
          },
          error: (err) => {
            console.error(`Error creating room type ${index}:`, err);
            errorCount++;
            this.checkCompletion(completedCount, errorCount);
          }
        });
      }
    });
  }
  
  checkCompletion(completed: number, errors: number) {
    const total = this.contract.roomTypes.length;
    if (completed + errors === total) {
      if (errors > 0) {
        alert(`Contract saved with ${errors} errors in room types. Please check the console for details.`);
      } else {
        alert('Contract and all room types saved successfully!');
      }
      this.router.navigate(['/contracts']);
    }
  }

 
}