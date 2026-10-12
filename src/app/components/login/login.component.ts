import { Component, NgZone, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { TokenStorageService } from '../../core/services/token-storage.service';
import { environment } from 'src/environments/environment';

declare const google: any;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private tokenService: TokenStorageService,
    private router: Router,
    private ngZone: NgZone
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
    this.initGoogleSignIn();
  }

  get f() { return this.loginForm.controls; }

  onSubmit(): void {
    if (this.loginForm.invalid) return;
    this.loading = true;
    this.errorMessage = '';

    this.authService.login(this.loginForm.value).subscribe({
      next: (res) => {
        this.tokenService.saveToken(res.token);
        this.tokenService.saveUser(res);
        this.successMessage = 'Connexion réussie';
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Identifiants invalides';
        this.loading = false;
      },
      complete: () => (this.loading = false)
    });
  }

  initGoogleSignIn(): void {
    if (typeof google === 'undefined') return;
    google.accounts.id.initialize({
      client_id: environment.googleClientId,
      callback: (response: any) => this.handleGoogleLogin(response)
    });
    google.accounts.id.renderButton(
      document.getElementById('google-btn'),
      { theme: 'outline', size: 'large', width: 320 }
    );
  }

  handleGoogleLogin(response: any): void {
    this.ngZone.run(() => {
      this.authService.loginWithGoogle(response.credential).subscribe({
        next: (res) => {
          this.tokenService.saveToken(res.token);
          this.tokenService.saveUser(res);
          this.router.navigate(['/home']);
        },
        error: () => (this.errorMessage = 'Échec de connexion Google')
      });
    });
  }
}