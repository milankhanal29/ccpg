import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {ProductApiService} from "../../admin-services/product-api.service";
import {ToasterService} from "../../../users/services/toaster.service";

@Component({
  selector: 'app-update-product',
  templateUrl: './update-product.component.html',
  styleUrls: ['./update-product.component.css']
})
export class UpdateProductComponent  implements OnInit {
  productForm: FormGroup;
  productId: number;
  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private productService: ProductApiService,
    private router:Router,
    private toasterService:ToasterService
  ) {
    this.productForm = this.formBuilder.group({
      productName: ['', Validators.required],
      productPrice: ['', Validators.required],
      productDescription: [''],
    });
    this.productId = +this.route.snapshot.paramMap.get('id')!;
  }

  ngOnInit(): void {
    this.productService.getProductById(this.productId).subscribe((product) => {
      this.productForm.patchValue({
        productName: product.productName,
        productPrice: product.productPrice,
        productDescription:product.productDescription,

      });
    });
  }
  onSubmit(): void {
    if (this.productForm.valid) {
      const updatedProduct = {
        id: this.productId,
        productName: this.productForm.value.productName,
        productPrice: this.productForm.value.productPrice,
        productDescription: this.productForm.value.productDescription,

      };

      this.productService.updateProduct(updatedProduct).subscribe(() => {
      this.toasterService.successMessage("Product updated Successfully","success")
       this.router.navigate(['/admin/'])
      },error => {
        this.toasterService.successMessage("Product updated Successfully","success")
        console.log(error)
        }
      );
    }
  }

}
