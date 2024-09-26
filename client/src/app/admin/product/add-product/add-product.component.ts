import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductApiService } from '../../admin-services/product-api.service';
import {ToasterService} from "../../../users/services/toaster.service";

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css'],
})
export class AddProductComponent implements OnInit {
  productForm: FormGroup;
  selectedFileUrl: string | null = null;
  selectedFile: File | null = null;

  constructor(
    private fb: FormBuilder,
    private productService: ProductApiService,
    private toasterService: ToasterService,
  ) {
    this.productForm = this.fb.group({
      productName: ['', Validators.required],
      productPrice: ['', Validators.required],
      productDescription: [''],
      image: [null, Validators.required],
    });
  }
  ngOnInit(): void {
    const savedFormData = localStorage.getItem('productForm');
    if (savedFormData) {
      this.productForm.patchValue({
      });
    }
  }
  onFileChange(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.productForm.patchValue({
        image: file,
      });
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.selectedFileUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }
  onSubmit() {
    if (this.productForm.valid && this.selectedFile) {
      const formData = new FormData();
      Object.keys(this.productForm.controls).forEach((key) => {
        formData.append(key, this.productForm.get(key)?.value);
      });
      formData.append('image', this.selectedFile, this.selectedFile.type);

      this.productService.saveProduct(formData).subscribe(
        (response: any) => {
          this.toasterService.successMessage("Product Added Successfully","success")

          console.log('Success:', response);
          this.selectedFileUrl = null;
        },
        (error: any) => {
          this.toasterService.errorMessage("Error saving product","error")
          // localStorage.setItem('productForm', JSON.stringify(this.productForm.value));

          console.error('Error:', error);
          console.error('Detailed error:', error.error);
        }
      );
    }
  }
}
