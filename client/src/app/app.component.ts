import {Component, OnInit} from '@angular/core';
import {AuthService} from "./users/shared/auth/auth.service";
import {HeaderService} from "./users/services/header.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'client';
  footerHeight:number=0;
  navHeight: number=0;
  totalUpDownHeight: number=0;
  constructor(private authService: AuthService,private headerService :HeaderService) {
  }
  userRole$ = this.authService.role$;
  ngOnInit() {
    this.headerService.getRoleAndEmail()
    const email=this.headerService.getRoleAndEmail().email
    const role=this.headerService.getRoleAndEmail().role
    console.log(email + role)

    this.authService.autoLogin().subscribe();
    const footer = document.querySelector('footer');
    if (footer !== null){
      this.footerHeight = footer.offsetHeight;
    }
    const nav = document.querySelector('nav');
    if (nav !== null){
      this.navHeight = nav.offsetHeight;
    }
    this.totalUpDownHeight=this.footerHeight+this.navHeight;
  }
}
