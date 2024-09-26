import {Injectable, OnInit} from '@angular/core';
import {BehaviorSubject, catchError, map, Observable, of, tap} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Endpoint} from "../constants/endpoint";
import {BackendService} from "../../services/backend.service";
import { JwtHelperService } from '@auth0/angular-jwt';
import {Router} from "@angular/router";
@Injectable({
  providedIn: 'root'
})
export class AuthService implements OnInit{
  ngOnInit() {
    const token = localStorage.getItem('token');
    console.log("tokrn is "+token)
  }
  private baseUrl = Endpoint.auth;
  private loggedIn = new BehaviorSubject<boolean>(false);
  isLoggedIn$: Observable<boolean> = this.loggedIn.asObservable();
  private role = new BehaviorSubject<string>('');
  role$: Observable<string> = this.role.asObservable();
  private loginUrl = `${this.baseUrl}/authenticate`;
  private signUpUrl = `${this.baseUrl}/register`;

  constructor(private http: HttpClient, private backendService: BackendService,private router:Router,private jwtHelper: JwtHelperService) {
    this.setLoggedIn(false);
    this.setRole('');
  }
  decodeToken(token: string): any {
    return this.jwtHelper.decodeToken(token);
  }

  setLoggedIn(value: boolean): void {
    this.loggedIn.next(value);
  }
  setRole(value: string): void {
    this.role.next(value);
  }
  login(email: string, password: string): Observable<any> {
    const credentials = { email, password };

    return this.http.post(this.loginUrl, credentials).pipe(
      tap(
        (response: any) => {
          const token = response.token;

          if (token) {
            const decodedToken = this.decodeToken(token);
            const userRole = decodedToken.role;

            this.setRole(userRole);
            this.setLoggedIn(true);
            localStorage.setItem('token', token);
          }
        },
        (error) => {
          console.error('Login failed', error);
        }
      )
    );
  }
  autoLogin(): Observable<boolean> {
    const token = localStorage.getItem('token');
    console.log("token is " + token);
    if (token) {
      const decodedToken = this.decodeToken(token);
      if (decodedToken && decodedToken.exp) {
        const expirationTimestamp = decodedToken.exp * 1000;
        const currentTimestamp = Date.now();
        if (currentTimestamp > expirationTimestamp) {
          this.setLoggedIn(false);
          return of(false);
        }
      }
      const userRole = decodedToken.role;

      this.setRole(userRole);
      this.setLoggedIn(true);

      return of(true);
    } else {
      this.setLoggedIn(false);
      return of(false);
    }
  }
  validateToken(token: string): Observable<boolean> {
    const validationEndpoint = 'hemlebanakourl';

    const headers = { Authorization: `Bearer ${token}` };

    return this.http.post<{ valid: boolean }>(validationEndpoint, {}, { headers }).pipe(
      map((response) => response.valid),
      catchError(() => of(false))
    );
  }
  logout(): void {
    localStorage.removeItem('token');
    this.setRole('');
    this.setLoggedIn(false);
    this.router.navigate(['/login'])
  }
  isAdmin(): boolean {
    return this.role.getValue() === 'admin';
  }
  signUp(formData: FormData): Observable<any> {
    return this.backendService.post(this.signUpUrl, formData);
  }
  getUserIdByEmail(userEmail: any): Observable<number> {
    const url = `/user/get-userid-by-email`;
    return this.http.post<number>(url, { email: userEmail });
  }

  }
