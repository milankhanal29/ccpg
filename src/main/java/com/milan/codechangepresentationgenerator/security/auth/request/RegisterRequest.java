package com.milan.codechangepresentationgenerator.security.auth.request;


import com.milan.codechangepresentationgenerator.shared.regexpcollection.RegExpMessageConstant;
import com.milan.codechangepresentationgenerator.shared.regexpcollection.RegexpsCollection;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Pattern(regexp = RegexpsCollection.NAME_PATTERN, message = RegExpMessageConstant.FIRST_NAME_VALIDATION_MSG)
    private String firstName;
    @Pattern(regexp = RegexpsCollection.MIDDLE_NAME_PATTERN, message = RegExpMessageConstant.MIDDLE_NAME_VALIDATION_MSG)
    private String middleName;

    @Pattern(regexp = RegexpsCollection.NAME_PATTERN, message = RegExpMessageConstant.LAST_NAME_VALIDATION_MSG)
    private String lastName;

    @Email(message = RegExpMessageConstant.EMAIL_VALIDATION_MSG)
    private String email;
    @NotNull(message = RegExpMessageConstant.NOT_BLANK)
    private String gender;
    @Pattern(regexp = RegexpsCollection.PHONE_PATTERN, message = RegExpMessageConstant.PHONE_VALIDATION_MSG)
    private String phone;
    @Pattern(regexp = RegexpsCollection.ADDRESS_PATTERN, message = RegExpMessageConstant.ADDRESS_VALIDATION_MSG)
    private String address;
    @Pattern(regexp = RegexpsCollection.PASSWORD_PATTERN, message = RegExpMessageConstant.PASSWORD_VALIDATION_MSG)
    private String password;
    @Pattern(regexp = RegexpsCollection.COUNTRY_PATTERN, message = RegExpMessageConstant.COUNTRY_NAME_VALIDATION_MSG)
    private String country;
    @Pattern(regexp = RegexpsCollection.CITY_PATTERN, message = RegExpMessageConstant.CITY_NAME_VALIDATION_MSG)
    private String city;
    @Pattern(regexp = RegexpsCollection.STREET_PATTERN, message = RegExpMessageConstant.STREET_NAME_VALIDATION_MSG)
    private String street;
    @Pattern(regexp = RegexpsCollection.STREET_NUMBER_PATTERN, message = RegExpMessageConstant.STREET_NUMBER_VALIDATION_MSG)
    private String streetNumber;

//    private String verificationCode;

}
