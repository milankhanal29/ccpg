package com.milan.codechangepresentationgenerator.user.entity;

import com.milan.codechangepresentationgenerator.security.auth.role.Role;
import com.milan.codechangepresentationgenerator.shared.abstractentity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_details")
public class User extends AbstractEntity implements UserDetails {
    @NotNull
    @Size(min = 2, max = 20)
    private String firstName;
    private String middleName;
    @NotNull
    @Size(min = 2, max = 20)
    private String lastName;
    @Email(message = "Please provide a valid email address")
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    private String gender;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Embedded
    private UserAddress userAddress;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
