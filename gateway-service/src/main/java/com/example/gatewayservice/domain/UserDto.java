package com.example.gatewayservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private String userName;
    private Collection<String> roles;
}