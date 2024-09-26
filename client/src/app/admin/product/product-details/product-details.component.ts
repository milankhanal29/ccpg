import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../../users/services/product.service";
import {ActivatedRoute} from "@angular/router";
import {IProduct} from "../../../interface/Product";

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit{
  product!: IProduct;
  productId!: number;

  constructor(private productService:ProductService,private route:ActivatedRoute) {
  }
  ngOnInit() {
    this.productId = +this.route.snapshot.paramMap.get('id')!;
    this.loadProductById();
  }

  loadProductById(): void {
    this.productService.getProductById(this.productId)
      .subscribe(
        (value: any | undefined) => {
          if (value) {
            this.product = value;
            value.decodedProductImage = 'data:image/png;base64,' + value.productImage;

            console.log('Product Details:', this.product);
          } else {
            console.error('Product not found');
          }
        },
        (error: any) => {
          console.error('Error fetching product details:', error);
        }
      );
  }
}
