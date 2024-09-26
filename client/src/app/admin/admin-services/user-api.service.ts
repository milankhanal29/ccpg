import { Injectable } from '@angular/core';
import {BackendService} from "../../users/services/backend.service";
import {catchError, map, Observable, throwError} from "rxjs";
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class UserApiService {
  constructor(private backendService: BackendService,private http:HttpClient) {
  }
  private baseUrl = 'http://localhost:8080/api/user';
  getUserById(id: number): Observable<any | undefined> {
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + localStorage.getItem('token'),
    });
   return this.http.get<any[]>(`${this.baseUrl}/get-by-id/${id}`,{ headers })
      .pipe(
        catchError(this.errorHandler)
      );
  }
  errorHandler(error:HttpErrorResponse)
  {
    return throwError(()=>new Error(error.message||"server error"))
  }
  getUserDetails(email: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + localStorage.getItem('token'),
    })
    const url = `${this.baseUrl}/user-details?email=${email}`;
    return this.http.get<any>(url,{ headers });
  }
  getUserNameById(userId: number): Observable<string> {
    return this.http.get(`${this.baseUrl}/get-user-name/${userId}`, { responseType: 'text' })
      .pipe(
        map((response: string) => response)
      );
  }

  getUserBalance(userId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/get-user-balance/${userId}`);
  }
}
