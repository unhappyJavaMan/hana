package com.hana.service.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class AuthRequest {
    @Data
    public static class LoginRequest {
        @NotBlank(message = "Account is required")
        private String account;

        @NotBlank(message = "Password is required")
        private String password;
    }
}
