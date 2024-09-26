import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public notifications: any,
             ) {
  }
  ngOnInit(): void {

  }
}
