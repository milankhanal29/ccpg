import {Component, OnInit} from '@angular/core';
import {UserApiService} from "../../admin-services/user-api.service";
import {ActivatedRoute} from "@angular/router";
import {IProduct} from "../../../interface/Product";

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit{
  user!: any ;
  userId!: number;

  constructor(private userService: UserApiService, private route: ActivatedRoute
  ) {}
  ngOnInit(): void {
    this.userId = +this.route.snapshot.paramMap.get('id')!;
    this.loadUserById();
  }
  loadUserById(): void {
    this.userService.getUserById(this.userId)
      .subscribe(
        (value: any | undefined) => {
          if (value) {
            this.user = value;
            console.log('User Details:', this.user);
          } else {
            console.error('User not found');
          }
        },
        (error: any) => {
          console.error('Error fetching User details:', error);
        }
      );
  }
}
