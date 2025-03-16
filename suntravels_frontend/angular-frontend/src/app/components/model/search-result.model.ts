import { RoomTypeResult } from './room-type-result.model';

export interface AssignedRoom {
  roomName: string;
  maxAdults: number;
  price: number;
  assignedAdults: number;
}

export interface SearchResult {
  hotelName: string;
  availableRoomTypes: RoomTypeResult[];
  assignedRooms?: AssignedRoom[];  // New: Assigned rooms per request
  totalPrice?: number;  // New: Total calculated price
}
