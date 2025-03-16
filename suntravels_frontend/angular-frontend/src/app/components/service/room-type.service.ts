import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RoomType } from '../model/room-type.model';

@Injectable({
  providedIn: 'root'
})
export class RoomTypeService {
  private apiUrl = 'http://localhost:8080/api/room-types';

  constructor(private http: HttpClient) {}

  getRoomTypesByContractId(contractId: number): Observable<RoomType[]> {
    return this.http.get<RoomType[]>(`${this.apiUrl}/contract/${contractId}`);
  }
  

  getRoomTypeById(id: number): Observable<RoomType> {
    return this.http.get<RoomType>(`${this.apiUrl}/get-room-type-by-id/${id}`);
  }

  createRoomType(roomType: RoomType): Observable<RoomType> {
    return this.http.post<RoomType>(`${this.apiUrl}/create-room-type`, roomType);
  }

  updateRoomType(id: number, roomType: RoomType): Observable<RoomType> {
    return this.http.put<RoomType>(`${this.apiUrl}/update-room-type/${id}`, roomType);
  }

  deleteRoomType(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete-room-type/${id}`);
  }
}