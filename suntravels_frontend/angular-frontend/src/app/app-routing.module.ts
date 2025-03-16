import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HotelListComponent } from './components/hotel/hotel-list/hotel-list.component';
import { HotelFormComponent } from './components/hotel/hotel-form/hotel-form.component';
import { ContractFormComponent } from './components/contract/contract-form/contract-form.component';
import { ContractListComponent } from './components/contract/contract-list/contract-list.component';
import { RoomTypeFormComponent } from './components/room-type/room-type-form/room-type-form.component';
import { RoomTypeListComponent } from './components/room-type/room-type-list/room-type-list.component';

const routes: Routes = [
  { path: 'hotels', component: HotelListComponent },
  { path: 'hotels/new', component: HotelFormComponent },
  { path: 'hotels/edit/:id', component: HotelFormComponent },

  // Contract Routes
  { path: 'contracts', component: ContractListComponent },
  { path: 'contracts/new', component: ContractFormComponent },
  { path: 'contracts/edit/:id', component: ContractFormComponent },

  // RoomType Routes
  { path: 'contracts/:contractId/room-types', component: RoomTypeListComponent },
  { path: 'contracts/:contractId/room-types/new', component: RoomTypeFormComponent },
  { path: 'contracts/:contractId/room-types/edit/:id', component: RoomTypeFormComponent },

  { path: '**', redirectTo: '/hotels' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
