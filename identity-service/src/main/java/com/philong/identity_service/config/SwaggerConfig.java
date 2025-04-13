package com.philong.identity_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SwaggerConfig {
    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Identity Service")
                        .version("1.0")
                )

                .components(new Components()

                        .addSecuritySchemes("bearerAuth",new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer") // client gửi token trong header
                                .bearerFormat("JWT")
                                .description("Enter your JWT token in the format")
                        ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}

// Bean OpenAPI cấu hình là nguồn dữ liệu để spingdoc openAPI tạo ra file OpenAPI Specification
// file OpenAPI Specification là một bản mô tả tiêu chuẩn về API
// mô tả này chủ yếu đc swagger ui sd để tạo ra giao diện tài liệu tương tác