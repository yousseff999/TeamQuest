import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
 // private apiUrl = 'http://localhost:8086/api/auth; // Uses the correct backend URL
private apiUrl = `${environment.apiBaseUrl}/api/auth;`
  constructor(private http: HttpClient, private router: Router) {}

  // Forgot Password
  forgotPassword(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/forgot-password`, { email }).pipe(
      catchError(error => {
        console.error('Forgot password error:', error);
        return throwError(() => ({
          message: error.error?.message || 'Failed to send reset link'
        }));
      })
    );
  }

  // Reset Password
  resetPassword(token: string, newPassword: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/reset-password`, { token, newPassword }).pipe(
      catchError(error => {
        console.error('Reset password error:', error);
        return throwError(() => ({
          message: error.error?.message || 'Failed to reset password'
        }));
      })
    );
  }

  // Signup
  signUp(name: string, email: string, password: string, confirmPassword: string): Observable<any> {
    const signUpData = { name, email, password, confirmPassword };
    return this.http.post<any>(`${this.apiUrl}/signup`, signUpData).pipe(
      tap(response => {
        if (response?.userId && response?.role && response?.token) {
          this.storeAuthData(response);
        }
      })
    );
  }

  // Login
  login(email: string, password: string): Observable<any> {
    const loginData = { email, password };
    return this.http.post<any>(`${this.apiUrl}/login`, loginData).pipe(
      tap(response => {
        if (response?.userId && response?.role && response?.token) {
          this.storeAuthData(response);
        }
      }),
      catchError(error => {
        console.error('Login error:', error);
        return throwError(() => ({
          message: error.error?.message || 'Login failed'
        }));
      })
    );
  }

  // Save user data
  private storeAuthData(response: any): void {
    localStorage.setItem('userId', response.userId.toString());
    localStorage.setItem('role', response.role);
    localStorage.setItem('token', response.token);
    if (response.username) {
      localStorage.setItem('username', response.username);
    }
  }

  // Logout
  logout(): void {
    localStorage.clear();
    sessionStorage.clear();
    this.router.navigate(['/login']);
  }

  // Getters
  getUserId(): string | null {
    return localStorage.getItem('userId');
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
