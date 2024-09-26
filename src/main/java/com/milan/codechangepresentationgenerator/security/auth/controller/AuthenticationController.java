package com.milan.codechangepresentationgenerator.security.auth.controller;
import com.milan.codechangepresentationgenerator.security.auth.request.AuthenticationRequest;
import com.milan.codechangepresentationgenerator.security.auth.request.RegisterRequest;
import com.milan.codechangepresentationgenerator.security.auth.response.AuthenticationResponse;
import com.milan.codechangepresentationgenerator.security.auth.service.AuthenticationService;
import com.milan.codechangepresentationgenerator.shared.endpoint.EndpointConstants;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EndpointConstants.AUTH_BASE_URL)
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping(EndpointConstants.API_REGISTER)
    public AuthenticationResponse register(@RequestBody RegisterRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping(EndpointConstants.API_AUTHENTICATE)
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.authenticate(authenticationRequest);
    }
}
