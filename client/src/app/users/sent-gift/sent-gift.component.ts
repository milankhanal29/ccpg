import {Component, OnInit} from '@angular/core';
import {HeaderService} from "../services/header.service";
import {ProductService} from "../services/product.service";
import {UserGiftService} from "../services/user-gift.service";
import {UserApiService} from "../../admin/admin-services/user-api.service";

@Component({
  selector: 'app-sent-gift',
  templateUrl: './sent-gift.component.html',
  styleUrls: ['./sent-gift.component.css'
  ]
})
export class SentGiftComponent implements OnInit {
  sentGifts: any[] = [];

  constructor(private headerService: HeaderService, private productService: ProductService, private userGiftService: UserGiftService, private userService: UserApiService) {
  }

  ngOnInit() {
    this.getSentGiftList()
  }
  toggleAdditionalProducts(gift: any): void {
    gift.showAdditionalProducts = !gift.showAdditionalProducts;
  }
  getSentGiftList() {
    const email = this.headerService.getRoleAndEmail().email;
    this.productService.getUserIdByEmail(email).subscribe((userId: any) => {
      this.userGiftService.getSentGifts(userId).subscribe((sentGifts: any[]) => {
        this.sentGifts = sentGifts;
        this.sentGifts.forEach(gift => {
          this.userService.getUserNameById(gift.receiverId).subscribe(
            receiverName =>
            {
              gift.receiverName = receiverName
            }, error => console.error('Error fetching receiver name:', error));

          gift.orderDetailsList.forEach((orderDetail: any) => {
            const productId = orderDetail.productId;
            this.productService.getProductNameById(productId).subscribe(productName => orderDetail.productName = productName, error => console.error('Error fetching product name:', error));
          });
        });
      }, error => {
        console.error('Error fetching sent gifts:', error);
      });
    });
  }

}
