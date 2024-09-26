package com.milan.codechangepresentationgenerator.user.service;


import com.milan.codechangepresentationgenerator.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    List<User> getAllUsers();

    User getUserById(int userId);

    User updateUserById(User user, int userId);

    void deleteUser(int userId);

    int getUserIdByEmail(String email);

    Optional<User> findByEmail(String email);

    String getUserNameById(int userId);

    User findUserByJwtToken(String jwtToken) throws Exception;
}
