package com.philong.identity_service.request;

import com.philong.identity_service.validation.DobConstraint;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @NotNull(message = "Username cannot null")
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;
    String password;
    String first_name;
    String last_name;
    @DobConstraint(min = 16, message = "DOB_INVALID")
    LocalDate dob;
    @Email(message = "EMAIL_INVALID")
    String email;
}
