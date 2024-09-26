import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "../../users/home/home/home.component";
import {UnauthorizedComponent} from "../../users/unauthorized/unauthorized.component";
import {LoginComponent} from "../../users/auth/login/login.component";
import {RegisterComponent} from "../../users/auth/register/register.component";
import {ProductComponent} from "../../users/product/product.component";
import {ErrorComponent} from "../../error/error.component";

const routes: Routes = [

  { path: '', component:HomeComponent},
  { path: 'unauthorized', component:UnauthorizedComponent},
  { path: 'login', component:LoginComponent},
  { path: 'sign-up', component:RegisterComponent},
  {path: 'product', component: ProductComponent},
  { path: '', redirectTo: '', pathMatch: "full" },
  { path: '**', component: ErrorComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SharedRoutingModule { }
