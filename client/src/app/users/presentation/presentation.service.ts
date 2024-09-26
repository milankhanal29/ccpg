// presentation.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PresentationService {
  private baseUrl = '/api/presentations'; // Adjust according to your API base URL

  constructor(private http: HttpClient) {}

  getPresentationsForUser(userEmail: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/user/${userEmail}`);
  }

  downloadPresentation(id: string): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/download/${id}`, { responseType: 'blob' });
  }
  deletePresentation(id: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
