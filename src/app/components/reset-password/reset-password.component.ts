import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-reset-password',
  template: `
    <div class="auth-container">
      <div class="auth-card">
        <h2>Réinitialiser le mot de passe</h2>

        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label>Nouveau mot de passe</label>
            <input type="password" formControlName="newPassword" class="form-control" />
          </div>

          <div class="form-group">
            <label>Confirmer</label>
            <input type="password" formControlName="confirmPassword" class="form-control" />
            <small *ngIf="form.errors?.['mismatch'] && form.touched" class="text-danger">
              Les mots de passe ne correspondent pas
            </small>
          </div>

          <button type="submit" class="btn-primary" [disabled]="loading">
            {{ loading ? 'Modification...' : 'Valider' }}
          </button>

          <div *ngIf="successMessage" class="alert alert-success">
            {{ successMessage }} <a routerLink="/login">Se connecter</a>
          </div>
          <div *ngIf="errorMessage" class="alert alert-danger">{{ errorMessage }}</div>
        </form>
      </div>
    </div>
  `
})
export class ResetPasswordComponent implements OnInit {
  form: FormGroup;
  token = '';
  loading = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
    if (!this.token) this.errorMessage = 'Token manquant ou invalide';
  }

  passwordMatchValidator(g: FormGroup) {
    return g.get('newPassword')?.value === g.get('confirmPassword')?.value
      ? null : { mismatch: true };
  }

  onSubmit(): void {
    if (this.form.invalid || !this.token) return;
    this.loading = true;
    this.errorMessage = '';

    this.authService.resetPassword(this.token, this.form.value.newPassword).subscribe({
      next: () => {
        this.successMessage = 'Mot de passe réinitialisé avec succès.';
        this.loading = false;
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Token expiré ou invalide';
        this.loading = false;
      }
    });
  }
}