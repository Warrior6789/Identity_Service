package com.philong.identity_service.service;

import com.nimbusds.jose.JOSEException;
import com.philong.identity_service.request.AuthenticationRequest;
import com.philong.identity_service.request.IntrospectRequest;
import com.philong.identity_service.request.LogoutRequest;
import com.philong.identity_service.request.RefreshRequest;
import com.philong.identity_service.response.AuthenticationResponse;
import com.philong.identity_service.response.IntrospectResponse;

import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    void Logout(LogoutRequest token) throws ParseException, JOSEException;
    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
