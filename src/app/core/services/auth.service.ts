import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  AuthenticationRequest,
  AuthenticationResponse,
  RegisterRequest,
  ResetPasswordDto
} from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private api = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {}

  register(request: RegisterRequest): Observable<any> {
    return this.http.post(`${this.api}/Register`, request);
  }

  login(request: AuthenticationRequest): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(`${this.api}/authenticate`, request);
  }

  loginWithGoogle(googleToken: string): Observable<AuthenticationResponse> {
    const params = new HttpParams().set('googleToken', googleToken);
    return this.http.post<AuthenticationResponse>(`${this.api}/google`, null, { params });
  }

  activateAccount(token: string): Observable<any> {
    const params = new HttpParams().set('token', token);
    return this.http.get(`${this.api}/activate-account`, { params });
  }

  forgotPassword(email: string): Observable<any> {
    const params = new HttpParams().set('email', email);
    return this.http.post(`${this.api}/forgot-password`, null,
      { params, responseType: 'text' as 'json' });
  }

  resetPassword(token: string, newPassword: string): Observable<any> {
    const params = new HttpParams()
      .set('token', token)
      .set('newPassword', newPassword);
    return this.http.post(`${this.api}/reset-password`, null, { params });
  }

  updatePassword(dto: ResetPasswordDto): Observable<any> {
    return this.http.post(`${this.api}/update-password`, dto);
  }
}