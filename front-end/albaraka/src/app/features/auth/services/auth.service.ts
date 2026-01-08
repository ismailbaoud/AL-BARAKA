import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'authToken';
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken());
  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {}

  login(email: string, password: string) {
    return this.http.post<{ token: string }>('http://localhost:8081/auth/login', { email, password }).pipe(
      tap(Response => {
        localStorage.setItem(this.tokenKey, Response.token);
        this.isAuthenticatedSubject.next(true);
        this.router.navigate(['/dashboard/client']);
      })
    );
  }

  //logout() {
  //  localStorage.removeItem(this.tokenKey);
  //  this.isAuthenticatedSubject.next(false);
  //  this.router.navigate(['/login']);
  //}

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  private hasToken(): boolean {
    return !!this.getToken();
  }
}