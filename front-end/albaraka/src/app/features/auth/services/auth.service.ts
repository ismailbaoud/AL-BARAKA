import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

export interface User {
  fullName: string;
  role: string;
  active: boolean;
  createdAt: string;
}

export interface LoginResponse {
  token: string;
  user: User;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly TOKEN_KEY = 'token';
  private readonly USER_KEY = 'current_user';
  private readonly EXPIRATION_KEY = 'token_expires_at';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  login(fullName: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>('http://localhost:8082/auth/login', { fullName, password }).pipe(
      tap(response => this.setSession(response))
    );
  }

  private setSession(authResult: LoginResponse): void {
    const token = authResult.token;
    const user = authResult.user;

    localStorage.setItem(this.TOKEN_KEY, token);

    localStorage.setItem(this.USER_KEY, JSON.stringify(user));

    const payload = JSON.parse(atob(token.split('.')[1]));
    const expiresAt = payload.exp * 1000;

    localStorage.setItem(this.EXPIRATION_KEY, expiresAt.toString());
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    localStorage.removeItem(this.EXPIRATION_KEY);
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    const expiration = this.getExpiration();
    return Date.now() < expiration && this.getToken() !== null;
  }

  isLoggedOut(): boolean {
    return !this.isLoggedIn();
  }

  getExpiration(): number {
    const expiration = localStorage.getItem(this.EXPIRATION_KEY);
    return expiration ? parseInt(expiration, 10) : 0;
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getCurrentUser(): User | null {
    const userStr = localStorage.getItem(this.USER_KEY);
    return userStr ? JSON.parse(userStr) : null;
  }
}