package com.mettle.auth.dto;

import javax.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message = "Username is required") String userName,
                           @NotBlank(message = "Password is required") String password) {
}
