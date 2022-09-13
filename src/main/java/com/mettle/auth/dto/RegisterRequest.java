package com.mettle.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    @NotBlank(message = "Username is required")
    private String userName;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Role is required")
    private String role;
}
