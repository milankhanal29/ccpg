import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from './admin-routing.module';
import {AddProductComponent} from "../../admin/product/add-product/add-product.component";
import {SharedModule} from "../shared/shared.module";
import {MaterialModule} from "../material/material.module";
import {UpdateProductComponent} from "../../admin/product/update-product/update-product.component";
import {AllProductComponent} from "../../admin/product/all-product/all-product.component";
import {OrderListComponent} from "../../admin/orders/order-list/order-list.component";
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {ProductDetailsComponent} from "../../admin/product/product-details/product-details.component";
import {UserDetailsComponent} from "../../admin/user/user-details/user-details.component";
import {ProductIdsDialogComponent} from "../../admin/product/product-ids-dialog/product-ids-dialog.component";
import {MatTableModule} from "@angular/material/table";
@NgModule({
  declarations: [
    AddProductComponent,
    UpdateProductComponent,
    AllProductComponent,
    OrderListComponent,
    ProductDetailsComponent,
    UserDetailsComponent,
    ProductIdsDialogComponent,


  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    SharedModule,
    MaterialModule,
    MatButtonToggleModule,
    MatTableModule
  ],
  exports: [
    AdminRoutingModule,
    AddProductComponent,
    AllProductComponent,
    OrderListComponent,
    ProductIdsDialogComponent,

  ]

})
export class AdminModule { }
