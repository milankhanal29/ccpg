import {Component, OnInit} from '@angular/core';
import {HeaderService} from "../services/header.service";
import {ProductService} from "../services/product.service";
import {UserGiftService} from "../services/user-gift.service";
import {UserApiService} from "../../admin/admin-services/user-api.service";

@Component({
  selector: 'app-received-gift',
  templateUrl: './received-gift.component.html',
  styleUrls: ['./received-gift.component.css']
})
export class ReceivedGiftComponent implements OnInit {
  receivedGifts: any[] = [];

  constructor(private headerService: HeaderService, private productService: ProductService, private userGiftService: UserGiftService, private userService: UserApiService) {
  }

  toggleAdditionalProducts(gift: any): void {
    gift.showAdditionalProducts = !gift.showAdditionalProducts;
  }

  ngOnInit() {
    this.getReceivedGift()
  }

  getReceivedGift() {
    const email = this.headerService.getRoleAndEmail().email;
    this.productService.getUserIdByEmail(email).subscribe((userId: any) => {
      this.userGiftService.getReceivedGifts(userId).subscribe((receivedGifts: any[]) => {
        this.receivedGifts = receivedGifts;
        this.receivedGifts.forEach(gift => {
          this.userService.getUserNameById(gift.senderId).subscribe(senderName => {
            console.log('Sender name:', senderName);
            gift.senderName = senderName;
          }, error => console.error('Error fetching sender name:', error));

          gift.orderDetailsList.forEach((orderDetail: any) => {
            const productId = orderDetail.productId;
            this.productService.getProductNameById(productId).subscribe(productName => orderDetail.productName = productName, error => console.error('Error fetching product name:', error));
          });
        });
      }, error => {
        console.error('Error fetching received gifts:', error);
      });
    });
  }
}
