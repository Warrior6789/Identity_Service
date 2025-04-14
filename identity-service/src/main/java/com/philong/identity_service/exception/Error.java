package com.philong.identity_service.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter

public enum Error {
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED,1001,"Unauthenticated"),
    UNAUTHORIZED(HttpStatus.FORBIDDEN,1002,"you do not have permission to access this resource"),
    USER_NOT_EXIST(HttpStatus.NOT_FOUND,1003,"User Not Exist"),
    ROLE_NOT_EXIST(HttpStatus.NOT_FOUND,1004,"Role Not Exist"),
    PERMISSION_NOT_EXIST(HttpStatus.NOT_FOUND,1005,"Permission Not Exist"),
    UNCATEGORIZED(HttpStatus.INTERNAL_SERVER_ERROR,9999,"Uncategorized"),
    ;
    HttpStatus httpStatusCode;
    int code;
    String message;
}
