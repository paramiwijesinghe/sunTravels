import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SearchResult } from '../model/search-result.model';
import { SearchRequest } from '../model/search-request.model';
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private baseUrl = environment.apiUrl + '/api/search'; // Backend search endpoint

  constructor(private http: HttpClient) {}

  searchAvailableRooms(searchRequest: SearchRequest): Observable<SearchResult[]> {
    return this.http.post<SearchResult[]>(`${this.baseUrl}`, searchRequest);
  }
}
