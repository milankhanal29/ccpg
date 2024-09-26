package com.milan.codechangepresentationgenerator.security.auth.service.impl;


import com.milan.codechangepresentationgenerator.security.auth.request.AuthenticationRequest;
import com.milan.codechangepresentationgenerator.security.auth.request.RegisterRequest;
import com.milan.codechangepresentationgenerator.security.auth.response.AuthenticationResponse;
import com.milan.codechangepresentationgenerator.security.auth.role.Role;
import com.milan.codechangepresentationgenerator.security.auth.service.AuthenticationService;
import com.milan.codechangepresentationgenerator.security.config.service.JwtService;
import com.milan.codechangepresentationgenerator.service.shared.MailService;
import com.milan.codechangepresentationgenerator.user.entity.User;
import com.milan.codechangepresentationgenerator.user.entity.UserAddress;
import com.milan.codechangepresentationgenerator.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl( UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    @Autowired
    private MailService mailService;

    public AuthenticationResponse register(RegisterRequest request) {
        var address = UserAddress.builder()
                .city(request.getCity())
                .street(request.getStreet())
                .country(request.getCountry())
                .streetNumber(request.getStreetNumber())
                .build();
        var user = User
                .builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender())
                .userAddress(address)
                .phone(request.getPhone())
                .gender(request.getGender())
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        var user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        mailService.sendEmail(authenticationRequest.getEmail(),"test","loggedin");
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
