package com.milan.codechangepresentationgenerator.security.auth.service;

import com.milan.codechangepresentationgenerator.security.auth.request.AuthenticationRequest;
import com.milan.codechangepresentationgenerator.security.auth.request.RegisterRequest;
import com.milan.codechangepresentationgenerator.security.auth.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
