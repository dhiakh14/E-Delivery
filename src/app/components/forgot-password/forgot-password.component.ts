import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-forgot-password',
  template: `
    <div class="auth-container">
      <div class="auth-card">
        <h2>Mot de passe oublié</h2>
        <p>Saisissez votre email pour recevoir un lien de réinitialisation.</p>

        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label>Email</label>
            <input type="email" formControlName="email" class="form-control" />
          </div>
          <button type="submit" class="btn-primary" [disabled]="loading">
            {{ loading ? 'Envoi...' : 'Envoyer' }}
          </button>

          <div *ngIf="successMessage" class="alert alert-success">{{ successMessage }}</div>
          <div *ngIf="errorMessage" class="alert alert-danger">{{ errorMessage }}</div>

          <p class="text-center mt-3">
            <a routerLink="/login">Retour à la connexion</a>
          </p>
        </form>
      </div>
    </div>
  `
})
export class ForgotPasswordComponent {
  form: FormGroup;
  loading = false;
  successMessage = '';
  errorMessage = '';

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.successMessage = this.errorMessage = '';

    this.authService.forgotPassword(this.form.value.email).subscribe({
      next: () => {
        this.successMessage = 'Un email de réinitialisation a été envoyé.';
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Aucun utilisateur trouvé avec cet email.';
        this.loading = false;
      }
    });
  }
}