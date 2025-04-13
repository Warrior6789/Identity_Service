package com.philong.identity_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.philong.identity_service.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import com.philong.identity_service.exception.Error;
import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Error error = Error.UNAUTHENTICATED;
        response.setStatus(error.getHttpStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(error.getCode())
                .message(error.getMessage())
                .build();
        ObjectMapper mapper = new ObjectMapper();

        response.getWriter().write(mapper.writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}
// tùy chỉnh phản hồi HTTP khi 1 yêu cầu đến 1 tài nguyên đc bảo vệ mà k có thông tin xác thực
// response.setStatus() thiết lập Status code cho phản hồi
// response.setContentType(MediaType.APPLICATION_JSON_VALUE) thiết lập nội dung content type cho phản hòi
// application/json điều này báo cho client biết rằng body của phản hồi ở dạng json
// MediaType.APPLICATION_JSON_VALUE là 1 hằng số trong spring framework đại diện cho "application/json"
// ObjectMapper mapper = new ObjectMapper();
// tạo instance của objectmapper từ thư viện jackson. sd để chuyển đổi đối tượng java thành chuỗi json
// response.getWriter().write(); lấy PrintWriter để viết dữ liệu vào body của phản hồi
// mapper.writeValueAsString() chuyển đổi đối tượng api thành 1 chuỗi json
// response.flushBuffer() đẩy bất kỳ dữ liêu nào còn lại trong buffer vào body của response đến client

// AuthenticationEntryPoint là interface trong spring security chịu trách nhiệm cho việc bắt đầu
// quy trình xác thực khi 1 yêu cầu đến 1 tài nguyên được bảo vệ mà k có thông tin xác thực hợp lệ
// khi 1 bộ lọc xác thực(vd: BasicAuthenticationFilter,BearerTokenAuthenticationFilter)
// phát hiện ra 1 yêu cầu ch xác thực thay vì trực tiếp ném 401 nó sẽ ủy quyền việc xử lý
// phản hồi 401 cho AuthenticationEntryPoint

