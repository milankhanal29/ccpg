import { Injectable } from '@angular/core';
import { ToastrService, IndividualConfig } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class ToasterService {
  constructor(private toastrService: ToastrService) {}

  successMessage(message: string, title: string): void {
    this.toastrService.success(message, title);
  }

  errorMessage(message: string, title: string): void {
    this.toastrService.error(message, title);
  }
  warningMessage(message: string, title: string): void {
    this.toastrService.warning(message, title);
  }
}
