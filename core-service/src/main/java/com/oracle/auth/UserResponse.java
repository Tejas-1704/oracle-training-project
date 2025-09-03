package com.oracle.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private Set<String> roles;
}
