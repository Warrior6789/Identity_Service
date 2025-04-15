package com.philong.identity_service.request;

import com.philong.identity_service.validation.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    String first_name;
    String last_name;
    @DobConstraint(min = 18, message = "DOB_INVALID")
    LocalDate dob;
    String email;
    List<String> roles;
}
