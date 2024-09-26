import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import { Router} from "@angular/router";

@Component({
  selector: 'app-product-ids-dialog',
  templateUrl: './product-ids-dialog.component.html',
  styleUrls: ['./product-ids-dialog.component.css']
})
export class ProductIdsDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: any,private router:Router) { }

  showProductDetails(productId: any) {
    this.router.navigate(['/admin/product/', productId]);

  }
}
