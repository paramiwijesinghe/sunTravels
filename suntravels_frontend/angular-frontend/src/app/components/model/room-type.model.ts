export interface RoomType {
  id?: number;
  name: string;
  pricePerPerson: number;
  numberOfRooms: number;
  maxAdults: number;
  contractId?: number;
}