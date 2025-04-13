package com.philong.identity_service.request;

import jakarta.persistence.Entity;
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
    String username;
    String password;
    String first_name;
    String last_name;
    LocalDate dob;
    String email;
}
