import { Component, OnInit } from '@angular/core';
import { ContractService } from '../../service/contract.service';
import { Contract } from '../../model/contract.model';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-contract-list',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './contract-list.component.html',
  styleUrls: ['./contract-list.component.css']
})
export class ContractListComponent implements OnInit {
  contracts: Contract[] = [];

  constructor(private contractService: ContractService, private router: Router) {}

  ngOnInit(): void {
    this.loadContracts();
  }

  // Load all contracts
  loadContracts() {
    this.contractService.getContracts().subscribe((data) => {
      this.contracts = data;
    });
  }

  
  // Navigate to edit contract page
  editContract(contractId: number) {
    this.router.navigate([`/contracts/edit`, contractId]);
  }
  

  // Delete a contract
  deleteContract(contractId: number) {
    if (confirm('Are you sure you want to delete this contract?')) {
      this.contractService.deleteContract(contractId).subscribe(() => {
        alert('Contract deleted successfully!');
        this.loadContracts();  // Reload contract list after deletion
      });
    }
  }

  // Navigate to the search page
  navigateToSearch() {
    this.router.navigate(['/search']);
  }

  // Navigate to hotel list page
  navigateToHotelList() {
    this.router.navigate(['/hotels']);
  }
}
