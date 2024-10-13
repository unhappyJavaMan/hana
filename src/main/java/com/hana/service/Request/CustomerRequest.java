package com.hana.service.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class CustomerRequest {
    @Data
    public static class Create {
        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        private String phone;
    }

    @Data
    public static class Update {
        @NotBlank(message = "Name is required")
        private String name;

        private String phone;
    }

    @Data
    public static class Delete {
        private Long id;
    }
}
