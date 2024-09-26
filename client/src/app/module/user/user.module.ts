import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserRoutingModule } from './user-routing.module';
import {CartComponent} from "../../users/cart/cart.component";
import {NavbarComponent} from "../../users/home/navbar/navbar.component";
import {CheckoutComponent} from "../../users/checkout/checkout.component";
import {RegisterComponent} from "../../users/auth/register/register.component";
import {MaterialModule} from "../material/material.module";
import {ProductModule} from "../product/product.module";
import {SharedModule} from "../shared/shared.module";
import {AdminModule} from "../admin/admin.module";
import {SentGiftComponent} from "../../users/sent-gift/sent-gift.component";
import {ReceivedGiftComponent} from "../../users/received-gift/received-gift.component";
import {MatExpansionModule} from "@angular/material/expansion";


@NgModule({
  declarations: [
    CartComponent,
    NavbarComponent,
    CheckoutComponent,
    RegisterComponent,
    SentGiftComponent,
    ReceivedGiftComponent,
  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    MaterialModule,
    ProductModule,
    SharedModule,
    AdminModule,
    MatExpansionModule,
  ],
  exports: [
    CommonModule,
    UserRoutingModule,
    CartComponent,
    NavbarComponent,
    CheckoutComponent,
    RegisterComponent,
    SentGiftComponent,
    ReceivedGiftComponent,
  ],
})
export class UserModule { }
