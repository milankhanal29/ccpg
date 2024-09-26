import {Component, OnInit} from '@angular/core';
import {CartService} from "../services/cart.service";
import {HeaderService} from "../services/header.service";
import {ProductService} from "../services/product.service";
import {ICart, IProduct} from "../../interface/Product";
import {ToasterService} from "../services/toaster.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit
{

  cartItems!: ICart[];

  constructor(private cartService: CartService,
              private headerService: HeaderService,
              private productService: ProductService,
              private toasterService:ToasterService,
              private router:Router,
)
  {}
  ngOnInit(): void {
    this.loadCartItems();
    this.cartService.cartUpdated.subscribe(() => {
      this.loadCartItems();
    });
  }
  loadCartItems() {
    const email = this.headerService.getRoleAndEmail().email;
    this.productService.getUserIdByEmail(email).subscribe((userId:any)=>{
      this.cartService.getActiveCartItems(userId).subscribe((data: ICart[]) => {
          this.cartItems = data;
          if (this.cartItems) {
            this.cartItems.forEach(cartItem => {
              cartItem.decodedProductImage = 'data:image/png;base64,' + cartItem.product.image;
              cartItem.quantity = 1;
            });
          }
        },
        (error) => {
          console.error('Error loading cart items:', error);
        }
      );
    })
  }

  removeCartItem(cartItemId: number) {
    this.cartService.removeCartItem(cartItemId).subscribe(
      (response: any) => {
        this.toasterService.successMessage(response.message, 'Success');
        this.loadCartItems();
      },
      (error) => {
        console.error('Error removing cart item:', error);
        this.toasterService.errorMessage('Error removing cart item.', 'Error');
      }
    );
  }
  clearCart() {
    const email = this.headerService.getRoleAndEmail().email;
    this.productService.getUserIdByEmail(email).subscribe((userId:any)=>{
    this.cartService.clearCart(userId).subscribe(
      (response: any) => {
        this.toasterService.successMessage(response.message, 'Success');
        this.loadCartItems();
      },
      (error) => {
        console.error('Error clearing the cart:', error);
        this.toasterService.errorMessage('Error clearing the cart.', 'Error');
      }
    );
  }
    )}

  navigateToCheckout(cartItems: ICart[]) {
    if (!cartItems || cartItems.length === 0) {
      console.error('No products provided.');
      return;
    }
    const productIdsWithQuantities = cartItems.map(item => ({ productId: item.product.id, quantity: item.quantity }));
    const productInfoJSON = JSON.stringify(productIdsWithQuantities);
    const encodedProductInfo = btoa(productInfoJSON);
    this.router.navigate(['/checkout'], {
      queryParams: { productInfo: encodedProductInfo },
    });
  }
  checkQuantity(cartItem: any) {
    if (cartItem.quantity > cartItem.product.availableQuantity) {
      cartItem.quantity = cartItem.product.availableQuantity;
      this.toasterService.warningMessage('Quantity exceeds available quantity!',"Warning");
    } else if (cartItem.quantity < 1) {
      cartItem.quantity = 1;
    }
  }

}
