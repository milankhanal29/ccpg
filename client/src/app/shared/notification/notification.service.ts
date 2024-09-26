import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  constructor(private http: HttpClient) { }
  getNotificationsByUserId(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`/api/notifications/get-by-user/${userId}`);
  }
  markNotificationAsRead(notificationId: number): Observable<void> {
    const url = `/api/notifications/mark-as-read/${notificationId}`;
    return this.http.put<void>(url, {});
  }
  getNotificationCount(userId: number): Observable<number> {
    return this.http.get<number>(`/api/notifications/get-notification-count/${userId}`);
  }
}
