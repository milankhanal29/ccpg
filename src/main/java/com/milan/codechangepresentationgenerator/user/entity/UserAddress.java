package com.milan.codechangepresentationgenerator.user.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddress {
    private String country;
    private String city;
    private String street;
    private String streetNumber;
}
