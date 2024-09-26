import {EventEmitter, Injectable, OnInit, Output} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {BackendService} from "./backend.service";
import {JwtHelperService} from "@auth0/angular-jwt";
import {CartService} from "./cart.service";
import {ProductService} from "./product.service";

@Injectable({
  providedIn: 'root'
})
export class HeaderService implements OnInit{
  cartUpdated: EventEmitter<void> = new EventEmitter<void>();

  constructor(private productService:ProductService,
              private backendService:BackendService,
              private jwtHelper:JwtHelperService,
              private cartService:CartService) {
    this.cartService.cartUpdated.subscribe(() => {
      this.cartCount();
    });

  }
  getRoleAndEmail(): { role: string, email: string } {
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken = this.jwtHelper.decodeToken(token);
      const role = decodedToken['role'];
      const email = decodedToken['sub'];
      return { role, email };
    } else {
      return { role: '', email: '' };
    }
  }
  ngOnInit() {
    this.getRoleAndEmail();
    this.cartCount();
  }
  cartCount(): Observable<number> {
    const email = this.getRoleAndEmail().email;
    return new Observable<number>((observer) => {
      this.productService.getUserIdByEmail(email).subscribe(
        (response: any) => {
          const userId = response;
          this.cartService.getActiveCartCount(userId).subscribe(
            (count) => {
              observer.next(count);
              observer.complete();
              this.cartUpdated.emit();
            },
            (error) => {
              console.error('Error fetching cart count:', error);
              observer.error(error);
            }
          );
        },
        (error) => {
          console.error('Error getting user ID by email:', error);
          observer.error(error);
        }
      );
    });
  }
}
