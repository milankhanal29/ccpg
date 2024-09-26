import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AuthGuard} from "../../users/shared/auth/auth.guard";
import {CartComponent} from "../../users/cart/cart.component";
import {CheckoutComponent} from "../../users/checkout/checkout.component";
import {PaymentComponent} from "../../payment/payment/payment.component";
import {PaymentSuccessComponent} from "../../payment/payment-success/payment-success.component";
import {PaymentFailureComponent} from "../../payment/payment-failure/payment-failure.component";
import {ReceivedGiftComponent} from "../../users/received-gift/received-gift.component";
import {SentGiftComponent} from "../../users/sent-gift/sent-gift.component";
import {SubscriptionComponent} from "../../users/subscription/subscription.component";
import {PresentationComponent} from "../../users/presentation/presentation.component";

const routes: Routes = [
  { path: 'cart', component: CartComponent, canActivate: [AuthGuard] ,data: { roles: ['USER']}},
  { path: 'sent-gift', component: SentGiftComponent, canActivate: [AuthGuard] ,data: { roles: ['USER']}},
  { path: 'received-gift', component: ReceivedGiftComponent, canActivate: [AuthGuard] ,data: { roles: ['USER']}},
  { path: 'checkout', component: CheckoutComponent, canActivate: [AuthGuard],data: { roles: ['USER']} },
  { path: 'subscription', component: SubscriptionComponent, canActivate: [AuthGuard],data: { roles: ['USER']} },
  { path: 'payment', component: PaymentComponent, canActivate: [AuthGuard],data: { roles: ['USER']} },
  { path: 'payment/success', component: PaymentSuccessComponent, canActivate: [AuthGuard],data: { roles: ['USER']} },
  { path: 'payment/failure', component: PaymentFailureComponent, canActivate: [AuthGuard],data: { roles: ['USER']} },
  { path: 'presentation', component: PresentationComponent, canActivate: [AuthGuard],data: { roles: ['USER']} },
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProductRoutingModule { }
