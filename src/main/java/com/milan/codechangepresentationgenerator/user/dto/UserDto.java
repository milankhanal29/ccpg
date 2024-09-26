package com.milan.codechangepresentationgenerator.user.dto;


import com.milan.codechangepresentationgenerator.user.entity.UserAddress;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private UserAddress address;
    private String email;
    private String phone;
}
