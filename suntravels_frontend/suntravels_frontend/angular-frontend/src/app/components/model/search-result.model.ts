import { RoomTypeResult } from './room-type-result.model';

export interface SearchResult {
  hotelName: string;
  availableRoomTypes: RoomTypeResult[];
}
