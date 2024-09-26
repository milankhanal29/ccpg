import {EventEmitter, Injectable} from '@angular/core';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {IProduct} from "../../interface/Product";
import {HttpClient} from "@angular/common/http";
@Injectable({
  providedIn: 'root'
})
export class CartService {
  cartUpdated: EventEmitter<void> = new EventEmitter<void>();
  private baseUrl = 'http://localhost:8080/api';
  private cartCountSubject = new BehaviorSubject<number>(0);
  cartCount$ = this.cartCountSubject.asObservable();
  private cartItemsSubject = new BehaviorSubject<IProduct[]>([]);
  cartItems$ = this.cartItemsSubject.asObservable();
  constructor(private http: HttpClient) {
  }
  getActiveCartItems(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/carts/user/${userId}`);
  }
  isProductInCart(userId: number, productId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/carts/is-product-in-cart`, { params: { userId: userId.toString(), productId: productId.toString() } });
  }
  removeCartItem(cartItemId: number): Observable<any> {
    const url = `${this.baseUrl}/carts/remove/${cartItemId}`;
    return this.http.put(url, null).pipe(
      tap(() => this.cartUpdated.emit())
    );
  }

  clearCart(userId:number): Observable<any> {
    const url = `${this.baseUrl}/carts/clear/${userId}`;
    return this.http.put(url, null).pipe(
      tap(() => this.cartUpdated.emit())
    );;
  }
  getActiveCartCount(userId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/carts/count/${userId}`);
  }
}
