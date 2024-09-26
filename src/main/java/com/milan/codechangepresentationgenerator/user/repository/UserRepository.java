package com.milan.codechangepresentationgenerator.user.repository;

import com.milan.codechangepresentationgenerator.security.auth.role.Role;
import com.milan.codechangepresentationgenerator.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByRole(Role role);
    Optional<User> findByRoleAndEmail(Role role, String email);

}
