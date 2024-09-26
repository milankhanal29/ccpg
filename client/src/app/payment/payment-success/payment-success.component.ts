import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { AuthService } from "../../users/shared/auth/auth.service";
import { OrderService } from "../service/order.service";
import { HeaderService } from "../../users/services/header.service";
import { ProductService } from "../../users/services/product.service";
import {ToasterService} from "../../users/services/toaster.service";

@Component({
  selector: 'app-payment-success',
  templateUrl: './payment-success.component.html',
  styleUrls: ['./payment-success.component.css']
})
export class PaymentSuccessComponent implements OnInit {
  transaction_code!: string;
  status!: string;
  amount: string | null = null;
  parsedData: any;
  isLoading: boolean = false;
  verificationStatus: string = '';

  constructor(private route: ActivatedRoute,
              private router: Router,
              private authService: AuthService,
              private orderService: OrderService,
              private headerService: HeaderService,
              private productService: ProductService,
              private toastrService:ToasterService,
  ) {}

  ngOnInit() {
    this.getPaymentSuccess();
    this.route.queryParams.subscribe(params => {
      const encodedData = params['data'];

      if (encodedData) {
        try {
          const decodedData = atob(encodedData);
          this.parsedData = JSON.parse(decodedData);
          this.transaction_code = this.parsedData.transaction_code;
          this.status = this.parsedData.status;
          this.amount = this.parsedData.total_amount;

          const orders = JSON.parse(localStorage.getItem('order') || '{}');
          const productIds = orders.products || [];


            const userEmail = this.headerService.getRoleAndEmail().email
            this.productService.getUserIdByEmail(userEmail).subscribe(
              (userId: any) => {
                console.log("Sender id is :", userId)

                this.orderService.saveOrder({
                  userId: userId,
                  paymentStatus: this.status,
                  transactionCode: this.transaction_code,
                  totalPrice: this.amount,
                  orderDetailsList: productIds.map((productId: number, index: number) => ({
                    productId: productId,
                  })),
                }).subscribe(
                  (response) => {
                    console.log('Order saved successfully:', response);
                    localStorage.removeItem('order');

                  },
                  (error) => {
                    console.error('Error saving order:', error);
                      this.toastrService.errorMessage(error.message,"");
                  }
                );
              });
        } catch (error) {
          console.error('Error decoding data:', error);
        }
      } else {
        console.error('No encoded data found in the query parameters.');
      }
    });
  }


  getPaymentSuccess() {
    this.route.queryParams.subscribe(params => {
      const encodedData = params['data'];
      if (encodedData) {
        const decodedData = atob(encodedData);
        try {
          this.parsedData = JSON.parse(decodedData);
        } catch (error) {
          console.error('Error parsing JSON:', error);
        }
      }
    });
  }

  decreaseProductQuantities(productIds: number[], quantities: number[]) {
    productIds.forEach((productId, index) => {
      const quantity = quantities[index];
      this.productService.decreaseProductQuantity(productId, quantity).subscribe(
        () => {
          console.log(`Product quantity decreased for product ID ${productId}`);
        },
        (error) => {
          console.error(`Error decreasing product quantity for product ID ${productId}:`, error);
        }
      );
    });
  }
}
