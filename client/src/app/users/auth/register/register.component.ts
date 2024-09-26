import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../shared/auth/auth.service";
import {ToasterService} from "../../services/toaster.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit{
  isLinear = true;
  sliderValue: number = 0;
  constructor(private authService: AuthService,
              private fb: FormBuilder,
              private toasterService:ToasterService,
              private route:Router,
  )  {
    this.signupForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
      middleName: ['', [Validators.minLength(2), Validators.maxLength(20)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
      email: ['', [Validators.email]],
      password: ['', [Validators.minLength(8)]],
      gender: [''],
      phone: [''],
      country: [''],
      city: [''],
      street: [''],
      streetNumber: [''],
      sliderValue: [0],
    });
  }
  signupForm: FormGroup;
  onSliderInput(event: any): void {
    const sliderValue = (event.target as HTMLInputElement).value;
    this.signupForm.get('sliderValue')?.setValue(sliderValue);
  }



  ngOnInit(): void {

  }

  signUp(): void {
    if (this.signupForm.valid) {
      const formData = this.signupForm.value;
      this.authService.signUp(formData).subscribe(
        (response) => {
          this.toasterService.successMessage("Successfully signed up ! Login here ","success");
          console.log('Signup successful', response);
          this.route.navigate(['/login'])
        },
        (error) => {
          this.toasterService.errorMessage("error signing up","Error");
          console.error('Signup failed', error);
        }
      );
    } else {
      this.toasterService.errorMessage("error signing up","Error");
      console.log('Form is not valid');
    }
  }
}
