import { Routes } from '@angular/router';
import { HotelListComponent } from './components/hotel/hotel-list/hotel-list.component';
import { HotelFormComponent } from './components/hotel/hotel-form/hotel-form.component';
import { ContractListComponent } from './components/contract/contract-list/contract-list.component';
import { ContractFormComponent } from './components/contract/contract-form/contract-form.component';
import { ReservationSearchComponent } from './components/search/reservation-search/reservation-search.component';

export const routes: Routes = [
  { path: '', component: HotelListComponent },
  { path: 'hotels', component: HotelListComponent },
  { path: 'hotels/new', component: HotelFormComponent },
  { path: 'hotels/edit/:id', component: HotelFormComponent },
  
  // Contract routes
  { path: '', component: ContractListComponent },
  { path: 'contracts', component: ContractListComponent },
  { path: 'contracts/edit/:id', component: ContractFormComponent },
  { path: 'contracts/new', component: ContractFormComponent },

  { path: 'search', component: ReservationSearchComponent }


];
