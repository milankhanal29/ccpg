import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {Endpoint} from "../../users/shared/constants/endpoint";

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private http: HttpClient) { }
  baseUrl=Endpoint.orderBaseUrl

  saveOrder(orderData: any): Observable<any> {
    const saveOrderUrl = Endpoint.saveOrder
    console.log('Order Data:', orderData);
    return this.http.post(saveOrderUrl, orderData).pipe(
      catchError(error => {
        console.error('Error occurred:', error);
        return throwError(error);
      })
    );;
  }
  getAllOrders(): Observable<any[]> {
    const getAllOrderUrl = Endpoint.getAllOrders
    return this.http.get<any[]>(getAllOrderUrl);
  }
  updateOrderStatus(orderId: number, newStatus: any): Observable<any> {
    const url = `${this.baseUrl}/${orderId}/status`;
    return this.http.post(url, { status: newStatus });
  }

}
