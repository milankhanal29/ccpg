import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {ImageModalComponent} from "../image-modal/image-modal.component";
import {FormBuilder} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {CartService} from "../services/cart.service";
import {ProductService} from "../services/product.service";
import { ICart, IProduct} from "../../interface/Product";
import {ToastrService} from 'ngx-toastr';
import {HeaderService} from "../services/header.service";
import {AuthService} from "../shared/auth/auth.service";

@Component({
  selector: 'app-product', templateUrl: './product.component.html', styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {
  cartCount: number = 0;
  products: IProduct[] | undefined;
  showFullDescription = false;

  toggleDescription() {
    this.showFullDescription = !this.showFullDescription;
  }

  constructor(private dialog: MatDialog,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private cartService: CartService,
              private productService: ProductService,
              private router: Router,
              private toastr: ToastrService,
              private headerService: HeaderService,
              private authService: AuthService,
  ) {
  }
  isLoggedIn$ = this.authService.isLoggedIn$;
  userRole$ = this.authService.role$;
  openImageModal(product: IProduct) {
    if (product && product.productImage) {
      const decodedProductImage = 'data:image/png;base64,' + product.productImage;
      this.dialog.open(ImageModalComponent, {
        data: {
          imageUrl: decodedProductImage,
          data: product.productDescription
        },
      });
    } else {
      console.error('No image found for the gift.');
    }
  }

  ngOnInit() {
    this.cartService.cartCount$.subscribe((count) => {
      this.cartCount = count;
    });
    this.productService.getProducts().subscribe((data: IProduct[]) => {
      this.products = data;
      if (this.products) {
        this.products.forEach(product => {
          product.decodedProductImage = 'data:image/png;base64,' + product.productImage;
        });
      }
    });
  }
  onCartSubmit(productId: number, quantity: number) {
    const email = this.headerService.getRoleAndEmail().email;
    this.productService.getUserIdByEmail(email).subscribe(
      (response: any) => {
        const userId = response;

        this.cartService.isProductInCart(userId, productId).subscribe(
          (isInCart: boolean) => {
            if (isInCart) {
              this.toastr.error('Item is already in the cart.', 'Error');
            } else {
              this.productService.addToCart({ userId, productId, quantity }).subscribe(
                (response: any) => {
                  console.log("response is" + response);
                  this.headerService.cartCount().subscribe(
                    (count) => {
                      this.cartCount = count;
                      console.log("Cart count updated:", this.cartCount);
                    },
                    (error) => {
                      console.error('Error updating cart count:', error);
                    }
                  );
                  this.toastr.success(`Product added to the cart. Product ID: ${productId}, User ID: ${userId}`, 'Success');
                },
                (error: any) => {
                  console.error('Error adding product to the cart:', error);
                  this.toastr.error('Error adding product to the cart.', 'Error');
                }
              );
            }
          },
          (error) => {
            console.error('Error checking if product is in cart:', error);
            this.toastr.error('Error checking if product is in cart.', 'Error');
          }
        );
      },
      (error) => {
        console.error('Error getting user ID by email:', error);
        this.toastr.error('Login to add items to cart.', 'Error');
      }
    );
  }

  onGiftSubmit(product: IProduct): void {
    if (!product) {
      console.error('No product provided.');
      return;
    }
    const productItem: any = {
      product: product,
      quantity: 1,
    };

    this.router.navigate(['/checkout'], {
      queryParams: { productInfo: this.getEncodedProductInfo([productItem]) },
    });
  }
  private getEncodedProductInfo(cartItems: ICart[]): string {
    const productIdsWithQuantities = cartItems.map(item => ({ productId: item.product.id, quantity: item.quantity }));
    const productInfoJSON = JSON.stringify(productIdsWithQuantities);
    return btoa(productInfoJSON);
  }

}

