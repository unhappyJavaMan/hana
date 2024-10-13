package com.hana.service.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

public class UserRequest {

    @Data
    public static class createUser {
        @NotNull(message = "nickName is required.")
        private String nickName;
        @NotNull(message = "account is required.")
        private String account;
        @NotNull(message = "password is required.")
        private String password;
        @NotNull(message = "email is required.")
        private String email;
        @NotNull(message = "phone is required.")
        private String phone;

        @NotNull(message = "roleIds is required.")
        private List<Long> roleIds;
    }

    @Data
    public static class Delete {
        @NotNull(message = "User ID is required")
        private Long id;
    }

    @Data
    public static class Update {
        @NotNull(message = "User ID is required")
        private Long id;
        @NotNull(message = "nickName is required.")
        private String nickName;
        @NotNull(message = "email is required.")
        private String email;
        @NotNull(message = "phone is required.")
        private String phone;
        @NotNull(message = "roleIds is required.")
        private List<Long> roleIds;
    }

    @Data
    public static class Get {
        @NotNull(message = "User ID is required")
        private Long id;
    }

    @Data
    public static class ChangePassword {
        @NotNull(message = "User ID is required")
        private Long id;
        @NotBlank(message = "Current password is required")
        private String currentPassword;
        @NotBlank(message = "New password is required")
        private String newPassword;
    }
}
