import { RoomRequest } from './room-request.model';

export interface SearchRequest {
  checkInDate: string; // Format: YYYY-MM-DD
  numberOfNights: number;
  roomRequests: RoomRequest[];
}
