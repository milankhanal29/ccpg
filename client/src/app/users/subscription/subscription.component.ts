import { Component, OnInit } from '@angular/core';
import { SubscriptionService } from "./subscription.service";
import { HeaderService } from "../services/header.service";
import { ProductService } from "../services/product.service";
import { IProduct } from "../../interface/Product";
import { ImageModalComponent } from "../image-modal/image-modal.component";
import { MatDialog } from "@angular/material/dialog";
import { Clipboard } from '@angular/cdk/clipboard';

@Component({
  selector: 'app-subscription',
  templateUrl: './subscription.component.html',
  styleUrls: ['./subscription.component.css']
})
export class SubscriptionComponent implements OnInit {
  subscriptions: any[] = [];
  products: { [key: number]: IProduct } = {};  // Store products keyed by ID
  webhookUrls: { [key: number]: string } = {};
  tooltipVisible: { [key: number]: boolean } = {};
  tooltipMessage: { [key: number]: string } = {};  constructor(
    private subscriptionService: SubscriptionService,
    private dialog: MatDialog,
    private productService: ProductService,
    private headerService: HeaderService,
    private clipboard: Clipboard
  ) {}

  ngOnInit() {
    this.loadUserSubscriptions();
  }

  loadUserSubscriptions() {
    const email = this.headerService.getRoleAndEmail().email;
    this.productService.getUserIdByEmail(email).subscribe((userId: any) => {
      this.subscriptionService.getUserSubscription(userId).subscribe(
        (subscriptions: any[]) => {
          this.subscriptions = subscriptions;
          this.subscriptions.forEach((subscription) => {
            this.fetchWebhookUrl(subscription.id);
            this.fetchProductDetails(subscription.productId);
            console.log('Expiry Date:', subscription.expiryDate);

          });
        },
        (error) => console.error('Error fetching subscriptions:', error)
      );
    });
  }

  fetchWebhookUrl(subscriptionId: number) {
    this.subscriptionService.getWebhookUrl(subscriptionId).subscribe(
      (response) => {
        this.webhookUrls[subscriptionId] = response.url;
      },
      (error) => console.error('Error fetching webhook URL:', error)
    );
  }

  fetchProductDetails(productId: number) {
    this.productService.getProductById(productId).subscribe(
      (product: IProduct) => {
        product.decodedProductImage = 'data:image/png;base64,' + product.productImage;
        this.products[productId] = product; // Store product by ID
      },
      (error) => console.error('Error fetching product details:', error)
    );
  }

  copyToClipboard(subscriptionId: number): void {
    const url = this.webhookUrls[subscriptionId];
    if (url) {
      this.clipboard.copy(url);
      this.tooltipVisible[subscriptionId] = true; // Show tooltip
      this.tooltipMessage[subscriptionId] = 'Webhook URL copied!'; // Set tooltip message

      // Hide tooltip after 2 seconds
      setTimeout(() => {
        this.tooltipVisible[subscriptionId] = false;
        this.tooltipMessage[subscriptionId] = ''; // Clear tooltip message
      }, 2000);
    }
  }
  calculateDaysUntilExpiry(expiryDate: string): number {
    const today = new Date();
    const expiry = new Date(expiryDate);
    const timeDiff = expiry.getTime() - today.getTime();
    const daysDiff = Math.ceil(timeDiff / (1000 * 3600 * 24));
    return daysDiff >= 0 ? daysDiff : 0;
  }

}
