import {Component, OnInit} from '@angular/core';
import {OrderService} from "../../../payment/service/order.service";
import {ProductService} from "../../../users/services/product.service";
import {Route, Router} from "@angular/router";
import {UserApiService} from "../../admin-services/user-api.service";
import {MatDialog} from "@angular/material/dialog";
import {ProductIdsDialogComponent} from "../../product/product-ids-dialog/product-ids-dialog.component";
import {Status} from "../../../payment/service/status";
@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent implements OnInit {
  orders: any[] = [];
  constructor(private orderService: OrderService,
              private productService :ProductService,
              private userService:UserApiService,
              private router:Router,
              private dialog:MatDialog
  ) {}

  ngOnInit(): void {
    this.loadOrders();

  }
  userDetails(userId: number): void {
    this.router.navigate(['/admin/user/', userId]);
  }
  loadOrders(): void {
    this.orderService.getAllOrders().subscribe(
      (data) => {
        this.orders = data;
      },
      (error) => {
        console.error('Error fetching orders', error);
      }
    );
  }
  onUpdateOrderStatus(orderId: number, currentStatus: any) {
    const newStatus = currentStatus === Status.PENDING ? Status.ACCEPTED : Status.PENDING;
    this.orderService.updateOrderStatus(orderId, newStatus).subscribe(
      () => {
        console.log('Order status updated successfully');
      },
      (error) => {
        console.error('Error updating order status:', error);
      }
    );
  }
  showProduct(orderDetailsList: any[]) {
    const productIds = orderDetailsList.map(orderDetail => orderDetail.productId);
    const dialogRef = this.dialog.open(ProductIdsDialogComponent, {
      data: {productIds},
      width: '500px'
    });
  }

}
