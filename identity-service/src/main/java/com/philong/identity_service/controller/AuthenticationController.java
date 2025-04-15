package com.philong.identity_service.controller;

import com.nimbusds.jose.JOSEException;
import com.philong.identity_service.request.AuthenticationRequest;
import com.philong.identity_service.request.IntrospectRequest;
import com.philong.identity_service.request.LogoutRequest;
import com.philong.identity_service.response.ApiResponse;
import com.philong.identity_service.response.AuthenticationResponse;
import com.philong.identity_service.response.IntrospectResponse;
import com.philong.identity_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse result = authenticationService.authenticate(request);
        ApiResponse<AuthenticationResponse> api = new ApiResponse<>();
        api.setResult(result);
        return api;

        // ApiResponse<AuthenticatedResponse>
        // nên t dùng builder để tạo ra authenticationResponse với trường authenticated là boolean
        // ApiResponse<AuthenticationResponse> có dạng
        // code: 1000
        // result: {
        // authenticated: true hoặc false
        // }
    }

    @PostMapping("/logout")
    ApiResponse<Void> authenticate(@RequestBody LogoutRequest request) {
        authenticationService.Logout(request);
        ApiResponse<Void> api = new ApiResponse<>();
        return api;
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request){
        ApiResponse<IntrospectResponse> api = new ApiResponse<>();
        try {
            IntrospectResponse result = authenticationService.introspect(request);
            api.setResult(result);
        }catch(JOSEException|ParseException e){
            log.error(e.getMessage());
        }
        return api;
    }
}
