import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpHandler, HttpInterceptor, HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenStorageService } from '../services/token-storage.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private tokenService: TokenStorageService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.tokenService.getToken();

    const isAuthUrl = req.url.includes('/auth/authenticate')
                   || req.url.includes('/auth/Register')
                   || req.url.includes('/auth/google')
                   || req.url.includes('/auth/forgot-password')
                   || req.url.includes('/auth/reset-password');

    if (token && !isAuthUrl) {
      req = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      });
    }
    return next.handle(req);
  }
}