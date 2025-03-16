import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoomTypeService {
  private apiUrl = 'http://localhost:8080/api/room-types';

  constructor(private http: HttpClient) {}

  getRoomTypesByContractId(contractId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/contract/${contractId}`);
  }

  getRoomTypeById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/get-room-type-by-id/${id}`);
  }

  createRoomType(roomType: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/create-room-type`, roomType);
  }

  updateRoomType(id: number, roomType: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/update-room-type/${id}`, roomType);
  }

  deleteRoomType(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete-room-type/${id}`);
  }
}
