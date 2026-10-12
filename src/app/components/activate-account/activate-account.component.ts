import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-activate-account',
  template: `
    <div class="auth-container">
      <div class="auth-card text-center">
        <h2>Activation du compte</h2>
        <div *ngIf="loading">Vérification en cours...</div>
        <div *ngIf="!loading && success" class="alert alert-success">
          Votre compte a été activé avec succès !
          <a routerLink="/login">Se connecter</a>
        </div>
        <div *ngIf="!loading && error" class="alert alert-danger">{{ error }}</div>
      </div>
    </div>
  `
})
export class ActivateAccountComponent implements OnInit {
  loading = true;
  success = false;
  error = '';

  constructor(private route: ActivatedRoute, private authService: AuthService) {}

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');
    if (!token) {
      this.loading = false;
      this.error = 'Token manquant';
      return;
    }
    this.authService.activateAccount(token).subscribe({
      next: () => { this.loading = false; this.success = true; },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message || 'Token invalide ou expiré';
      }
    });
  }
}