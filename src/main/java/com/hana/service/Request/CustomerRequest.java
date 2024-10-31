package com.hana.service.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

public class CustomerRequest {
    @Data
    public static class Create {
        @NotBlank(message = "name is required")
        private String name;

        @NotBlank(message = "email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "phone is required")
        private String phone;

        private String gender;

        private LocalDate birthDate;
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
