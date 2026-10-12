import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { RegisterRequest } from '../../core/models/auth.model';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      dateOfBirth: [''],
      role: ['CLIENT', Validators.required],
      cin: [null],
      matricule: ['']
    });
    this.onRoleChange();
  }

  get f() { return this.registerForm.controls; }

  onRoleChange(): void {
    const role = this.registerForm.get('role')?.value;
    if (role === 'CLIENT') {
      this.registerForm.get('cin')?.setValidators([Validators.required]);
      this.registerForm.get('matricule')?.clearValidators();
    } else {
      this.registerForm.get('matricule')?.setValidators([Validators.required]);
      this.registerForm.get('cin')?.clearValidators();
    }
    this.registerForm.get('cin')?.updateValueAndValidity();
    this.registerForm.get('matricule')?.updateValueAndValidity();
  }

  onSubmit(): void {
    if (this.registerForm.invalid) return;
    this.loading = true;
    this.errorMessage = '';

    const payload: RegisterRequest = { ...this.registerForm.value };

    this.authService.register(payload).subscribe({
      next: () => {
        this.successMessage = 'Compte créé ! Vérifiez votre email pour activer votre compte.';
        setTimeout(() => this.router.navigate(['/login']), 2500);
      },
      error: (err) => {
        this.errorMessage = err?.error?.error || 'Erreur lors de l\'inscription';
        this.loading = false;
      }
    });
  }
}