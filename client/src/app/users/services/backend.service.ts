import { Injectable } from '@angular/core';
import {catchError, Observable, throwError} from "rxjs";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class BackendService {

  constructor(
    private http: HttpClient
  ) {}

  get(path: string, params: HttpParams = new HttpParams()): Observable<any> {
    return this.http.get(path, {params})
      .pipe(catchError(this.handleError));
  }

  post(path: string, body: object = {}): Observable<any> {
    return this.http.post(path,body)
      .pipe(catchError(this.handleError));
  }

  put(path: string, body: object = {}, header: object = {}): Observable<any> {
    return this.http.put(path, body)
      .pipe(catchError(this.handleError));
  }
  handleError(error: any) {
    return throwError(error);
  }
}
