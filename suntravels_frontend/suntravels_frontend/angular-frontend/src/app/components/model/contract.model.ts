import { RoomType } from './room-type.model';

export interface Contract {
  id?: number;
  hotelId: number;
  hotelName: string;
  startDate: string;
  endDate: string;
  markupPercentage: number;
  roomTypes: RoomType[];  
}
