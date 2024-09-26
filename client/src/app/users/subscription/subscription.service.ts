import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {

  constructor(private http: HttpClient) { }
  getUserSubscription(userId: number): Observable<any[]> {
  return this.http.get<any[]>(`/api/orders/subscription/${userId}`);
}
  getWebhookUrl(subscriptionId: number): Observable<{ url: string }> {
    return this.http.get<{ url: string }>(`/api/dashboard/webhook-url/${subscriptionId}`);
  }

}
