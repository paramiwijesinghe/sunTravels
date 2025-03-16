import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, switchMap, map } from 'rxjs';
import { Contract } from '../model/contract.model';
import { RoomType } from '../model/room-type.model';


@Injectable({ providedIn: 'root' })
export class ContractService {
  private apiUrl = 'http://localhost:8080/api/contracts'; // Adjust URL if necessary

  constructor(private http: HttpClient) {}

  getContracts(): Observable<Contract[]> {
    return this.http.get<Contract[]>(`${this.apiUrl}/getallcontracts`);
  }

  getContractById(id: number): Observable<Contract> {
    return this.http.get<Contract>(`${this.apiUrl}/${id}`).pipe(
      switchMap(contract => {
        // Fetch room types for this contract
        return this.http.get<RoomType[]>(`http://localhost:8080/api/room-types/contract/${id}`).pipe(
          map(roomTypes => {
            contract.roomTypes = roomTypes;
            return contract;
          })
        );
      })
    );
  }

  addContract(contract: Contract): Observable<Contract> {
    return this.http.post<Contract>(`${this.apiUrl}/createcontract`, contract);
  }

  updateContract(id: number, contract: Contract): Observable<Contract> {
    return this.http.put<Contract>(`${this.apiUrl}/updatecontract/${id}`, contract);
  }

  deleteContract(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/deletecontract/${id}`);
  }
}
