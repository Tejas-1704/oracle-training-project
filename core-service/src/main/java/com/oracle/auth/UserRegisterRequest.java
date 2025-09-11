package com.oracle.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserRegisterRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String role;

    // Optional fields to support creating/associating a customer on registration
    private String email; // used for customer if provided
    private String customerId; // link to existing customer, if any

    // Flattened customer fields (for convenience)
    private String firstName;
    private String lastName;
    private String phone;
    private String dob; // ISO-8601 (YYYY-MM-DD)

    // Nested customer payload option
    private CustomerDto customer;

    @Data
    @NoArgsConstructor
    public static class CustomerDto {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String dob; // ISO-8601 (YYYY-MM-DD)
    }
}
