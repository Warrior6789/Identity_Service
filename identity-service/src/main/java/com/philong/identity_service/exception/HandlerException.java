package com.philong.identity_service.exception;

import com.philong.identity_service.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class HandlerException {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException e) {
        Error error = e.getError();
        ApiResponse response = new ApiResponse();
        response.setMessage(error.getMessage());
        response.setCode(error.getCode());
        return ResponseEntity.status(error.getHttpStatusCode()).body(response);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e) {
        Error error = Error.UNAUTHORIZED;
        return ResponseEntity.status(error.getHttpStatusCode()).body(
                ApiResponse.builder()
                        .code(error.getCode())
                        .message(error.getMessage()).
                        build()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
        ApiResponse response = new ApiResponse();
        response.setMessage(Error.UNCATEGORIZED.getMessage());
        response.setCode(Error.UNCATEGORIZED.getCode());
        return ResponseEntity.badRequest().body(response);
    }
}

// lỗi Authentication thường trả về bởi bộ lọc(filter) xác thực của security khi nó phát
// hiện 1 yêu cầu ch xác thực cố gắng truy cập 1 tài nguyên yêu cầu xác thực