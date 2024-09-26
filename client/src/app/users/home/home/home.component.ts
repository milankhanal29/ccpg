import { Component } from '@angular/core';
import {AuthService} from "../../shared/auth/auth.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  constructor(private authService:AuthService) {
  }
  isLoggedIn$ = this.authService.isLoggedIn$;
  userRole$ = this.authService.role$;
}
