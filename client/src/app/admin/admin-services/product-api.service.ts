import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Endpoint} from "../../users/shared/constants/endpoint";
import {BackendService} from "../../users/services/backend.service";

@Injectable({
  providedIn: 'root'
})
export class ProductApiService {
  private getApiUrl = Endpoint.products;
  private postApiUrl = Endpoint.addProduct;
  constructor(private backendService: BackendService,private http: HttpClient) {}
  saveProduct(formData: FormData): Observable<any> {
    return this.backendService.post(this.postApiUrl, formData);
  }
  getProductById(productId: number): Observable<any> {
    const url=`${this.getApiUrl}/get-product/${productId}`;
    return this.http.get(url)
  }
  updateProduct(updatedProduct: any): Observable<any> {
    const url = `${this.getApiUrl}/update/${updatedProduct.id}`;
    return this.http.put(url, updatedProduct);
  }
}
