package com.philong.identity_service.config;

import com.philong.identity_service.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Value("${security.jwt.signkey}")
    private String signKey;
    private final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/log-in",
            "/api/v1/auth/introspect",
            // đây là thư mục chứa các tài nguyên tĩnh(html/css/...)
            // đây là endpoint mà springdoc openapi 3.0 tạo ra cung cấp file cấu hình api dạng json
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request ->{
            request.requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINTS).permitAll()
                    .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated();

        });
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.oauth2ResourceServer(oauth2 -> {
            oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                    .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                    .authenticationEntryPoint(new JwtAuthenticationEntryPoint());
        });
        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signKey.getBytes(), "HS512");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}

//Endpoint /v3/api-docs đóng vai trò là nguồn cung cấp thông tin cấu trúc API
// của bạn dưới dạng một file theo chuẩn OpenAPI. SpringDoc OpenAPI tự động tạo ra
// file này bằng cách phân tích mã controller và các annotations API của bạn.
// Swagger UI sau đó sử dụng file này để hiển thị tài liệu API một cách trực quan
// và cho phép bạn tương tác với API.

// kích hoạt oauth2ResourceServer: khi b gọi phương thức oauth2ResourceServer() trong cấu hình HttpSecurity
// b đang nói rằng "ứng dụng này là 1 OAuth2 Resource Server" điều này là nó sẽ
// bảo vệ các endpoints và chỉ cho phép truy cập khi ng dùng cung cấp access token
// hợp lệ được cấp bởi OAuth2 Authorization Server
// Tham số duy nhất Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>> là 1 functional interface
// đây là interface chịu trách nhiệm cung cấp các tùy chọn cấu hình cho OAuth2 Resource Server
// khi gọi oauth2.jwt() chỉ định resource server sd jwt(json web token) để xác thực access token

// spring sucurity chuyển đổi jwt thành đối tượng authentication mà spring security có thể hiểu và làm việc
// JwtAuthenticationConvertter là ng phiên dịch trong quá trình này nó nhận đầu vào là jwt
// và đầu ra là authentication và nó chứa thông tin quan trọng về ng dùng
// JwtGrantedAuthoritiesConverte là công cụ nhỏ để thêm tiền tố "_ROLE" vào mỗi quyền mà nó đọc được
// vd: jwt có scope là admin và user thì trở thành ROLE_admin và ROLE_user
