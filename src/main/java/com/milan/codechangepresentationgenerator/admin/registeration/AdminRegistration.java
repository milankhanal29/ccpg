package com.milan.codechangepresentationgenerator.admin.registeration;


import com.milan.codechangepresentationgenerator.admin.FileReaderClass;
import com.milan.codechangepresentationgenerator.security.auth.role.Role;
import com.milan.codechangepresentationgenerator.shared.status.Status;
import com.milan.codechangepresentationgenerator.user.entity.User;
import com.milan.codechangepresentationgenerator.user.entity.UserAddress;
import com.milan.codechangepresentationgenerator.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AdminRegistration implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileReaderClass fileReaderClass;

    @Value("admin@gmail.com")
    private String username;

    @Value("admin@gmail.com")
    private String password;

    @Override
    public void run(String... args) throws Exception {
//        fileReaderClass.readXLSfile();

        Optional<User> isExistedAdmin = userRepository.findByRole(Role.ADMIN);
        if (isExistedAdmin.isEmpty()) {
            log.info("\nRegistration of Admin is proceeding...");
            var adminAddress = UserAddress.builder()
                    .country("Nepal").city("Kathmandu").street("New-Baneshwor").streetNumber("03").build();
            User admin = new User();
            admin.setFirstName("milan");
            admin.setMiddleName("");
            admin.setLastName("khanal");
            admin.setEmail(username);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setPhone("9808036220");
            admin.setRole(Role.ADMIN);
            admin.setGender("Male");
            admin.setUserAddress(adminAddress);
            admin.setStatus(Status.ACTIVE);
            userRepository.save(admin);
            log.info("Admin is successfully registered..");
        }
    }
}


