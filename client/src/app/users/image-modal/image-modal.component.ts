import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-image-modal',
  template: `
    <body>
      <img [src]="data.imageUrl" alt="Full Image">
      <p>{{ data.description }}</p>
    </body>
  `,
  styles: [
    `
      img {
        display: block;
        margin-left: auto;
        margin-right: auto;
        width: 50%;
        overflow: hidden;
      }
      body {
        background: #f588f0;
      }
    `,
  ],
})
export class ImageModalComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {}
}
