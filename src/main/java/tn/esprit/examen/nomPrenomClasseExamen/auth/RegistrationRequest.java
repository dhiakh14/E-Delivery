package tn.esprit.examen.nomPrenomClasseExamen.auth;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty @NotBlank
    private String firstName;
    @NotEmpty @NotBlank
    private String lastName;

    @Past
    private LocalDate dateOfBirth;

    @Email @NotEmpty @NotBlank
    private String email;

    @NotEmpty @NotBlank @Size(min = 8)
    private String password;

    // Pour CLIENT
    private Long cin;

    // Pour EMPLOYER
    private String matricule;

    @NotNull
    private String role; // "CLIENT" ou "EMPLOYER"
}