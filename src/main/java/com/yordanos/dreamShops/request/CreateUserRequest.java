package com.yordanos.dreamShops.request;

import com.yordanos.dreamShops.dto.AddressDto;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
