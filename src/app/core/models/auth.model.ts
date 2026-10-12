export interface AuthenticationRequest {
  email: string;
  password: string;
}

export interface AuthenticationResponse {
  token: string;
  userId?: number;
  fullName?: string;
  email?: string;
  role?: string;
  details?: any;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  dateOfBirth?: string;
  email: string;
  password: string;
  role: string;          // "CLIENT" | "EMPLOYER"
  cin?: number | null;
  matricule?: string | null;
}

export interface ResetPasswordDto {
  email: string;
  currentPassword: string;
  newPassword: string;
}