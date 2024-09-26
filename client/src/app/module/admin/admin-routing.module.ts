import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AddProductComponent} from "../../admin/product/add-product/add-product.component";
import {AuthGuard} from "../../users/shared/auth/auth.guard";
import {DashboardComponent} from "../../admin/dashboard/dashboard.component";
import {AllProductComponent} from "../../admin/product/all-product/all-product.component";
import {UpdateProductComponent} from "../../admin/product/update-product/update-product.component";
import {OrderListComponent} from "../../admin/orders/order-list/order-list.component";
import {ProductDetailsComponent} from "../../admin/product/product-details/product-details.component";
import {UserDetailsComponent} from "../../admin/user/user-details/user-details.component";

const routes: Routes = [
  {
    path: 'admin',
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
    children: [
      { path: '', component: AllProductComponent },
      { path: 'update-product/:id', component: UpdateProductComponent },
      { path: 'orders', component: OrderListComponent },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'add-product', component: AddProductComponent },
      { path: 'product/:id', component: ProductDetailsComponent },
      { path: 'user/:id', component: UserDetailsComponent },

    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
