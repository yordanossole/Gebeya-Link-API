package com.yordanos.dreamShops.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private AddressDto address;
}
