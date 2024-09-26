import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import * as CryptoJS from 'crypto-js';
import {ActivatedRoute} from "@angular/router";
@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit{
  esewaForm!: FormGroup;
  receiverDetails: any;
  orderDetail:any
  productIds: number[] = [];
  quantities: number[] = [];
  constructor(private fb: FormBuilder, private route: ActivatedRoute)
  {
    this.createForm(0, 0, 0)
  }
  ngOnInit() {
     localStorage.removeItem('receiver');
     localStorage.removeItem('order');
    const encodedOrderDetails = this.route.snapshot.queryParamMap.get('orderDetails');

    if (encodedOrderDetails) {
      const decodedOrderDetails = JSON.parse(atob(encodedOrderDetails));
      const {amount, tax_amount, total_amount } = decodedOrderDetails;
      this.receiverDetails = {
        email: decodedOrderDetails.email,
        message: decodedOrderDetails.message,
      };
      this.orderDetail = {
        totalPrice: decodedOrderDetails.total_amount,
      };

      this.productIds = decodedOrderDetails.products || [];
      this.quantities = decodedOrderDetails.quantities || [];
      this.createForm(amount, tax_amount, total_amount);
      console.log(decodedOrderDetails);
    } else {
      console.error('No order details found in the query parameters.');
    }
  }
  createForm(amount: number, taxAmount: number, totalAmount: number) {
    this.esewaForm = this.fb.group({
      amount: [amount, Validators.required],
      tax_amount: [taxAmount, Validators.required],
      total_amount: [totalAmount, Validators.required],
      transaction_uuid: ['', Validators.required],
      product_code: ['EPAYTEST', Validators.required],
      product_service_charge: [0, Validators.required],
      product_delivery_charge: [0, Validators.required],
      success_url: ['http://localhost:4200/payment/success', Validators.required],
      failure_url: ['http://localhost:4200/payment/failure', Validators.required],
      signed_field_names: ['total_amount,transaction_uuid,product_code', Validators.required],
      signature: ['', Validators.required],
    });
  }
  submitMyFormBro() {
    const receiverDetails = {
      email: this.receiverDetails.email,
      message: this.receiverDetails.message,
    };
    const orderDetails = {
      products: this.productIds,
      quantities: this.quantities,
      totalAmount: this.esewaForm.value.total_amount,
      taxAmount: this.esewaForm.value.tax_amount,
    };
    localStorage.setItem('receiver', JSON.stringify(receiverDetails));
    localStorage.setItem('order', JSON.stringify(orderDetails));
    this.generateUUIDandSignature();
    const myform = document.createElement('form');
    myform.method = 'POST';
    myform.enctype = 'application/x-www-form-urlencoded';
    myform.action = 'https://rc-epay.esewa.com.np/api/epay/main/v2/form'; myform.style.display = 'none';
    for (const key in this.esewaForm.value) {
      if (this.esewaForm.value.hasOwnProperty(key)) {
        const field = document.createElement('input'); field.type = 'text';
        field.name = key;
        field.value = this.esewaForm.value[key]; myform.appendChild(field);
      }
    }
    document.body.appendChild(myform);
    myform.submit(); }
  generateUUIDandSignature() {
    const randomNumber = () => Math.floor(Math.random() * 10);
    const digitString =
      `${randomNumber()}${randomNumber()}-${randomNumber()}${randomNumber()}${randomNumber()}-${randomNumber()}${randomNumber()}`;
    this.esewaForm.patchValue({transaction_uuid: digitString});
    let total_amount = (document.getElementById("total_amount") as HTMLInputElement)?.value;
    let transaction_uuid = (document.getElementById("transaction_uuid") as HTMLInputElement)?.value;
    let product_code = (document.getElementById("product_code") as HTMLInputElement)?.value;
    let secret = (document.getElementById("secret") as HTMLInputElement)?.value;
    let hash =
      CryptoJS.HmacSHA256(`total_amount=${total_amount},transaction_uuid=${transaction_uuid},product_code=${product_code}`, `${secret}`);
    let hashInBase64 = CryptoJS.enc.Base64.stringify(hash); this.esewaForm.patchValue({signature: hashInBase64});
  }
}
