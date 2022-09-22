package com.mettle.auth.dto;

import javax.validation.constraints.NotBlank;

public record RegisterRequest(String firstName, String lastName, @NotBlank(message = "Username is required") String userName,
        @NotBlank(message = "Password is required") String password, @NotBlank(message = "Role is required") String role) {
}
