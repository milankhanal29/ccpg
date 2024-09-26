import {Component, OnInit} from '@angular/core';
import {IProduct} from "../../../interface/Product";
import {ProductService} from "../../../users/services/product.service";
import {Router} from "@angular/router";
import {ToasterService} from "../../../users/services/toaster.service";

@Component({
  selector: 'app-all-product',
  templateUrl: './all-product.component.html',
  styleUrls: ['./all-product.component.css']
})
export class AllProductComponent implements OnInit{
  products!:IProduct[];
  i!: number;
  constructor(private productService:ProductService,private router:Router,private toasterService :ToasterService) {
  }
  ngOnInit() {
    this.loadProducts();
  }
  loadProducts()
  {
    this.productService.getProducts().subscribe((data: IProduct[]) => {
      this.products = data;
      if (this.products) {
        this.products.forEach(product => {
          product.decodedProductImage = 'data:image/png;base64,' + product.productImage;
        });
      }
    });
  }
  editProduct(productId: number): void {
    this.router.navigate(['/admin/update-product', productId]);
  }

  deleteProduct(productId: number): void {
    this.productService.deleteProduct(productId).subscribe(
      ()=>
      {
        this.toasterService.successMessage("product Deleted Successfully", 'Success');
        this.loadProducts();

      }, (error) => {
        console.error('Error deleting product:', error);
        this.toasterService.errorMessage('Error deleting product', 'Error');
      }
    )
  }
}

