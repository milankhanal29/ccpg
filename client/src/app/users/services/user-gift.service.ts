import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserGiftService {
  constructor(private http: HttpClient) { }
  getReceivedGifts(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`/api/orders/receiver/${userId}`);
  }
  getSentGifts(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`/api/orders/sender/${userId}`);
  }
}
