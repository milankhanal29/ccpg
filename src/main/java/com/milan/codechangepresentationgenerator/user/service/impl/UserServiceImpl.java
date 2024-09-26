package com.milan.codechangepresentationgenerator.user.service.impl;


import com.milan.codechangepresentationgenerator.security.config.utils.JwtProvider;
import com.milan.codechangepresentationgenerator.shared.exception.custom.ResourceNotFoundException;
import com.milan.codechangepresentationgenerator.shared.status.Status;
import com.milan.codechangepresentationgenerator.user.entity.User;
import com.milan.codechangepresentationgenerator.user.repository.UserRepository;
import com.milan.codechangepresentationgenerator.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
    }

    @Override
    public User updateUserById(User user, int userId) {

        User existingUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        existingUser.setUserAddress(user.getUserAddress());
        userRepository.save(existingUser);
        return existingUser;
    }

    @Override
    public void deleteUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with id " + userId));
        user.setStatus(Status.DELETED);
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public int getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found for email: " + email));
        return user.getId();
    }

    @Override
    public String getUserNameById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return user.getFirstName() + " " + user.getLastName();
    }

    @Override
    public User findUserByJwtToken(String jwtToken) throws Exception {
        log.info("User information is shown by using JWT Token.");
        String email = jwtProvider.getEmailFromJwtToken(jwtToken);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new Exception("User Not Found Exception.");
        }
        return user.get();
    }
}
