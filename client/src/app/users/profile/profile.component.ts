import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {AuthService} from "../shared/auth/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {
  showAmount: boolean = false;
  profilePic = {
    profilePhoto: localStorage.getItem('profilePhoto') || ''
  };
  constructor(@Inject(MAT_DIALOG_DATA) public userDetails: any,private router:Router, private authService:AuthService) {}
  showBalance() {
    this.showAmount = !this.showAmount;
  }
  logout() {
    this.authService.logout();
  }
  openFileInput(event: Event) {
    event.preventDefault();
    const fileInput = document.getElementById('fileInput');
    if (fileInput) {
      fileInput.click();
    }
  }
  handleFileInput(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        if (reader.result) {
          const imageDataURL = reader.result.toString();
          localStorage.setItem('profilePhoto', imageDataURL);
          this.profilePic.profilePhoto = imageDataURL;
        }
      };
      reader.readAsDataURL(file);
    }
  }
  getUserProfilePhoto() {
    return this.profilePic.profilePhoto || 'placeholder-image-url';
  }

  onClickSent() {
    this.router.navigate(['sent-gift'])
  }

  onClickReceived() {
    this.router.navigate(['received-gift'])
  }
}
