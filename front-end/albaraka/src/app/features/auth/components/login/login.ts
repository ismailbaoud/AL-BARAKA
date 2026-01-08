import { Component, signal } from '@angular/core';
import {form, Field} from '@angular/forms/signals';
import { AuthService } from '../../services/auth.service';

interface LoginData {
  email: string;
  password: string;
}

@Component({
  selector: 'app-login',
  imports: [Field],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  constructor(private authService: AuthService) { }

  loginModel = signal<LoginData>({
    email: '',
    password: '',
  });

  loginForm = form(this.loginModel);
    onSubmit() {
    const email = this.loginForm.email().value();
    const password = this.loginForm.password().value();

    this.authService.login(email, password)
      .subscribe({
        next: res => console.log('JWT:', res.token),
        error: err => console.error('Login failed', err)
    });
  }
  
}
