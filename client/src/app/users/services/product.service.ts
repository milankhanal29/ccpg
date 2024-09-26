import { Injectable } from '@angular/core';
import {catchError, map, Observable, of, throwError} from "rxjs";
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from "@angular/common/http";
import {IProduct} from "../../interface/Product";
import {Endpoint} from "../shared/constants/endpoint";
@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private baseApiUrl = Endpoint.products;
  private getAllProduct = Endpoint.getAllProducts;

  private baseUrl = 'http://localhost:8080/api';
  constructor(private http: HttpClient) {}
  getProducts(): Observable<IProduct[]> {
    return this.http.get<IProduct[]>(`${this.getAllProduct}`)
      .pipe(catchError(this.errorHandler));
  }
  getProductById(id: number): Observable<IProduct> { // Updated to return a single product
    return this.http.get<IProduct>(`${this.baseApiUrl}/get-product/${id}`)
      .pipe(
        catchError(this.errorHandler)
      );
  }

  errorHandler(error:HttpErrorResponse)
  {
    return throwError(()=>new Error(error.message||"server error"))
  }
  getUserIdByEmail(email: string) {
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + localStorage.getItem('token'),
    });
    return this.http.get(`${this.baseUrl}/user/get-userid-by-email`, { headers, params: { email } });
  }
  addToCart(cartData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/carts/add`, cartData, { responseType: 'text' }).pipe(
      catchError((error) => {
        if (error instanceof HttpErrorResponse) {
          if (error.status === 200) {
            return of(error);
          } else {
            console.error('Unexpected error adding product to the cart:', error);
            return throwError('Unexpected error adding product to the cart.');
          }
        }
        return throwError(error);
      })
    ) as Observable<any>;
  }
  deleteProduct(productId:number):Observable<any>
  {
    const url = `${this.baseApiUrl}/delete/${productId}`;
    return this.http.put(url, null);

  }
  getProductsByIds(productIds: number[]): Observable<IProduct[]> {
    const idsParam = productIds.join(',');
    return this.http.get<IProduct[]>(`${this.baseApiUrl}/get-product-by-ids`, { params: { ids: idsParam } });
  }
  decreaseProductQuantity(productId: number, quantity: number): Observable<any> {
    const url = `${this.baseApiUrl}/decrease-quantity/${productId}`;
    const params = new HttpParams().set('quantity', quantity.toString());
    return this.http.put(url, {}, { params });
  }
  getProductNameById(productId: number): Observable<string> {
    return this.http.get(`${this.baseApiUrl}/get-product-name/${productId}`,{ responseType: 'text'})
      .pipe(map((response: string) => response)
    );
  }
}
