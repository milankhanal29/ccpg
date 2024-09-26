import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../shared/auth/auth.service";
import {CartService} from "../../services/cart.service";
import {HeaderService} from "../../services/header.service";
import {MatDialog} from "@angular/material/dialog";
import {UserApiService} from "../../../admin/admin-services/user-api.service";
import {ProfileComponent} from "../../profile/profile.component";
import {ProductService} from "../../services/product.service";
import {NotificationComponent} from "../../../shared/notification/notification.component";
import {NotificationService} from "../../../shared/notification/notification.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  cartCount!: number;
  private profileData: any;
  notificationCount: any;

  constructor(
    private authService: AuthService,
    private cartService: CartService,
    private headerService: HeaderService,
    private userService: UserApiService,
    private productService: ProductService,
    public dialog: MatDialog,
    private notificationService: NotificationService
  ) {
  }
  isNavbarOpen = true;
  isLoggedIn$ = this.authService.isLoggedIn$;
  userRole$ = this.authService.role$;

  ngOnInit() {
    this.getNotificationCount()
    this.cartService.cartUpdated.subscribe(() => {
      this.updateCartCount();
    });
    this.updateCartCount();
  }
  getNotificationCount() {
    const email = this.headerService.getRoleAndEmail().email;
    this.productService.getUserIdByEmail(email).subscribe((userId: any) => {
      this.notificationService.getNotificationCount(userId).subscribe(
        count => {
          this.notificationCount = count;
        },
        error => {
          console.error('Error fetching notification count:', error);
        });
    });
  }
  openProfileDialog(): void {
    const userEmail = this.headerService.getRoleAndEmail().email;
    this.productService.getUserIdByEmail(userEmail).subscribe(
      (userId: any) => {
        this.userService.getUserById(userId).subscribe(
          (userDetails: any) => {
            this.dialog.open(ProfileComponent, {
              data: userDetails
            });
          },
          error => {
            console.error('Error getting user details:', error);
          }
        );
      },
      error => {
        console.error('Error getting user ID:', error);
      }
    );
  }

  openNotificationDialog() {
    const email = this.headerService.getRoleAndEmail().email;
    this.productService.getUserIdByEmail(email).subscribe((userId: any) => {
      this.notificationService.getNotificationsByUserId(userId).subscribe(
        (notifications: any[]) => {
          for (const notification of notifications) {
            if (!notification.read) {
              this.notificationService.markNotificationAsRead(notification.id).subscribe(
                () => {
                  console.log(`Notification ${notification.id} marked as read.`);
                },
                (error) => {
                  console.error(`Error marking notification ${notification.id} as read:`, error);
                }
              );
            }
          }
          this.dialog.open(NotificationComponent, {
            data: notifications
          });
        },
        (error) => {
          console.error('Error fetching notifications', error);
        }
      );
    });
  }

  logout() {
    this.authService.logout();
  }

  updateCartCount() {
    this.headerService.cartCount().subscribe(
      (count) => {
        this.cartCount = count;
        console.log("count is " + this.cartCount);
      },
      (error) => {
        console.error('Error fetching cart count:', error);
      }
    );
  }


}
