import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../shared/auth/auth.service";
import {Router} from "@angular/router";
import {ToasterService} from "../../services/toaster.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService,private router :Router, private toasterService:ToasterService) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    localStorage.removeItem('token')
  }

  login(): void {
    if (this.loginForm.valid) {
      const email = this.loginForm.get('email')!.value;
      const password = this.loginForm.get('password')!.value;

      this.authService.login(email, password).subscribe(
        (response) => {
          this.router.navigate(['/'])
        this.toasterService.successMessage("successfully loggedin","success")
          console.log('Login successful', response);
        },
        (error) => {
          this.toasterService.errorMessage("failed to login","error")
          console.error('Login failed', error);
        }
      );
    }
  }
}
