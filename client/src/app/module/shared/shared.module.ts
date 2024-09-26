import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SharedRoutingModule } from './shared-routing.module';
import {HttpClientModule} from "@angular/common/http";
import {FlexLayoutModule} from "@angular/flex-layout";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {BrowserModule} from "@angular/platform-browser";
import {ErrorComponent} from "../../error/error.component";
import {ImageModalComponent} from "../../users/image-modal/image-modal.component";
import {ProductModule} from "../product/product.module";

@NgModule({
  declarations: [
    ErrorComponent,
    ImageModalComponent,
  ],
  imports: [
    CommonModule,
    SharedRoutingModule,
    HttpClientModule,
    FlexLayoutModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    FormsModule,
    BrowserModule,
    ProductModule,
  ],
  exports: [
    SharedRoutingModule,
    HttpClientModule,
    FlexLayoutModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    FormsModule,
    BrowserModule,
    ProductModule,
    ErrorComponent,
    ImageModalComponent,
  ]
})
export class SharedModule { }
