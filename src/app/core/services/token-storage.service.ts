import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class TokenStorageService {
  private TOKEN_KEY = 'auth-token';
  private USER_KEY  = 'auth-user';

  saveToken(token: string): void { localStorage.setItem(this.TOKEN_KEY, token); }
  getToken(): string | null { return localStorage.getItem(this.TOKEN_KEY); }

  saveUser(user: any): void { localStorage.setItem(this.USER_KEY, JSON.stringify(user)); }
  getUser(): any {
    const u = localStorage.getItem(this.USER_KEY);
    return u ? JSON.parse(u) : null;
  }

  clear(): void { localStorage.clear(); }

  isLoggedIn(): boolean { return !!this.getToken(); }

  getRole(): string | null {
    const u = this.getUser();
    return u ? u.role : null;
  }
}