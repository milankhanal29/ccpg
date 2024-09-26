import {Component, OnInit} from '@angular/core';
import {IProduct} from "../../interface/Product";
import {ProductService} from "../services/product.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ToasterService} from "../services/toaster.service";
import {UserApiService} from "../../admin/admin-services/user-api.service";
import {HeaderService} from "../services/header.service";

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  receiverForm!: FormGroup;
  initialEmail!: string;
  isLoading: boolean = false;
  private loggedInUserId!: number;

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router,
    private toasterService: ToasterService,
    private userService: UserApiService,
    private headerService: HeaderService
  ) {
  }

  products: IProduct[] = [];
  calculateTotalPrice(): number {
    return this.products.reduce((total, product) => total + product.productPrice, 0);
  }
  ngOnInit() {
    this.receiverForm = new FormGroup({
      email: new FormControl('', [Validators.email, Validators.required]),
      message: new FormControl('', [Validators.required])
    });
    this.receiverForm.get('email')?.valueChanges.subscribe((value: string) => {
      this.initialEmail = value;
    });
    this.route.queryParams.subscribe(params => {
      const encodedProductInfo: string | null = params['productInfo'];

      if (encodedProductInfo) {
        const productInfoJSON: string = atob(encodedProductInfo);
        const productInfo: { productId: number, quantity: number }[] = JSON.parse(productInfoJSON);

        this.getProductDetails(productInfo);
      } else {
        console.error('No product information provided.');
      }
    });
  }

  submitOrder() {
      const productIds = this.products.map(product => product.id);


      const orderDetails = {
        products: productIds,
        totalAmount: this.calculateTotalPrice(),
      };
      const encodedOrderDetails = btoa(JSON.stringify({
        products: productIds,
        amount: orderDetails.totalAmount,
        tax_amount: 0,
        total_amount: orderDetails.totalAmount,
      }));
      this.router.navigate(['/payment'], {
        queryParams: {
          orderDetails: encodedOrderDetails,

        },
      });
  }

  getProductDetails(productInfo: { productId: number, quantity: number }[]) {
    const productIds: number[] = productInfo.map(item => item.productId);
    this.productService.getProductsByIds(productIds).subscribe(
      (data: IProduct[] | null) => {
        if (data) {
          this.products = data.map(product => {
            product.decodedProductImage = 'data:image/png;base64,' + product.productImage;
            return product;
          });
          const totalAmount = this.calculateTotalPrice();
        } else {
          console.error('Received null data from getProductsByIds');
        }
      },
      error => {
        console.error('Error fetching product details:', error);
      }
    );
  }


}
