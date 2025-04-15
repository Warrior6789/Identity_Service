package com.philong.identity_service.exception;

import com.philong.identity_service.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Slf4j
@ControllerAdvice
public class HandlerException {

    private static final String MIN_ATTRIBUTE = "min";
    private static final String MAX_ATTRIBUTE = "max";

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
        log.error("RuntimeException", e);
        ApiResponse response = new ApiResponse();
        response.setMessage(Error.UNCATEGORIZED.getMessage());
        response.setCode(Error.UNCATEGORIZED.getCode());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage();
        Map<String, Object> attributes = null;
        ApiResponse response = new ApiResponse();
        Error error = Error.ENUM_KEY_NOT_FOUND;
        try {

            error = Error.valueOf(enumKey);
            var constrainViolation = e.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);
            attributes = constrainViolation.getConstraintDescriptor().getAttributes();
            log.info(attributes.toString());


        }catch(IllegalArgumentException ex) {
            log.error("IllegalArgumentException", ex);
        }

        response.setMessage(Objects.nonNull(attributes) ? mapAttribute(error.getMessage(),attributes) : error.getMessage());
        // Objects là class trong java.util
        response.setCode(error.getCode());
        return ResponseEntity.status(error.getHttpStatusCode()).body(response);
    }



    private String mapAttribute(String message, Map<String,Object> attributes){
        // message là thông báo ban đầu chứa placeholder {min}
        // attributes.get(MIN_ATTRIBUTE).toString()

        var minValue = attributes.get(MIN_ATTRIBUTE);

        if(minValue != null){
         message = message.replace("{" + MIN_ATTRIBUTE + "}", minValue.toString());
        }

        var maxValue = attributes.get(MAX_ATTRIBUTE);
        if(maxValue != null){
            message = message.replace("{" + MAX_ATTRIBUTE + "}", maxValue.toString());
        }

        return message;
    }
}

// lỗi Authentication thường trả về bởi bộ lọc(filter) xác thực của security khi nó phát
// hiện 1 yêu cầu ch xác thực cố gắng truy cập 1 tài nguyên yêu cầu xác thực

// valueof() là method tĩnh đc sd để chuyển đổi 1 chuỗi thành 1 instance của enum đó dựa trên tên của hằng số
// hàm getFieldError() và getDefaulMessage() sd để lấy thông tin chi tiết về 1 lỗi validation cụ thể
// liên quan đến trường cụ thể của đối tượng đc validate
// getDefaultMessage() trả về thông báo lỗi mặc định đc cấu hình trong annotation validation(vd: thông báo trong @size,@NotNull....)
// getCode() trả về mã lỗi(vd: size,notnull,...)
// getField() trả về tên của trường bị lỗi



//BindingResult chứa đối tượng lỗi FieldError và ObjectError đc tạo ra trong quá trình validation, và những đối tượng này tham chiếu đến các thông tin về constraint đã vi phạm
// BindingResult chứa kq của quá trình binding và validation gồm danh sách lỗi(FieldError và ObjectError)
// method getConstraintDescription() trả về đối tượng, nó cung cấp metadata về constraint annotation đã gây lỗi
// vd: @size, @notblank ...
// method getAttributes() trả về map, và map này chứa các thuộc tính của annotation validaton đã vi phạm
// map này có key là các tên thuộc tính của annotation và value là giá trị tương ứng
// vd @size(min = 8, max = 20, message = "mk phải có độ dài từ 8 đến 20")
// khi ng dùng gửi pass với độ dài là 5 quá trình validation sẽ thất bại và fieldError đc thêm vào BindingResult
// ContraintViolation sẽ đại diện cho lỗi vi phạm constarint @size nếu unwrap thành công
// attributes sẽ là map chứa key là min và value là 8 và key max value là 10