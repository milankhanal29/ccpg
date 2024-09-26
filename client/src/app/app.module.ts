import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FooterComponent} from './users/home/footer/footer.component';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {UnauthorizedComponent} from './users/unauthorized/unauthorized.component';
import {LoginComponent} from './users/auth/login/login.component';
import {ToastrModule} from "ngx-toastr";
import {JwtModule} from "@auth0/angular-jwt";
import {RequestInterceptor} from "./shared/interceptor/request.interceptor";
import {MaterialModule} from "./module/material/material.module";
import {ProductModule} from "./module/product/product.module";
import {UserModule} from "./module/user/user.module";
import {AdminModule} from "./module/admin/admin.module";
import {SharedModule} from "./module/shared/shared.module";
import {HomeComponent} from "./users/home/home/home.component";
import { PaymentComponent } from './payment/payment/payment.component';
import { PaymentSuccessComponent } from './payment/payment-success/payment-success.component';
import { PaymentFailureComponent } from './payment/payment-failure/payment-failure.component';
import { ProfileComponent } from './users/profile/profile.component';
import { NotificationComponent } from './shared/notification/notification.component';
import { SubscriptionComponent } from './users/subscription/subscription.component';
import { PresentationComponent } from './users/presentation/presentation.component';
export function tokenGetter() {
  return localStorage.getItem("token");
}
@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    UnauthorizedComponent,
    LoginComponent,
    HomeComponent,
    PaymentComponent,
    PaymentSuccessComponent,
    PaymentFailureComponent,
    ProfileComponent,
    NotificationComponent,
    SubscriptionComponent,
    PresentationComponent,
  ],

  imports: [
    ToastrModule.forRoot({
      timeOut: 3000,
      preventDuplicates: true,
      closeButton: true,
      onActivateTick: true,
      positionClass: 'toast-bottom-right',
    }),
    JwtModule.forRoot({
      config: {
        tokenGetter: tokenGetter,
      },
    }),
    AppRoutingModule,
    MaterialModule,
    ProductModule,
    AdminModule,
    UserModule,
    SharedModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: RequestInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
