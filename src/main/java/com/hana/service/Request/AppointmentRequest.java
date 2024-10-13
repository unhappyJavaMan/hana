package com.hana.service.Request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentRequest {


    @Data
    public static class Create {
        @NotNull(message = "Customer ID is required")
        private Long customerId;

        @NotNull(message = "User ID is required")
        private Long userId;

        @NotNull(message = "Appointment date is required")
        @Future(message = "Appointment date must be in the future")
        private LocalDate appointmentDate;

        @NotNull(message = "Start time is required")
        private LocalTime startTime;

        @NotNull(message = "End time is required")
        private LocalTime endTime;

        @AssertTrue(message = "End time must be after start time")
        private boolean isValidTimeRange() {
            return endTime != null && startTime != null && endTime.isAfter(startTime);
        }
    }

    @Data
    public static class Update {
        @NotNull(message = "Appointment ID is required")
        private Long id;

        private LocalDate appointmentDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private String status;

        @AssertTrue(message = "End time must be after start time")
        private boolean isValidTimeRange() {
            return (endTime == null || startTime == null) || endTime.isAfter(startTime);
        }
    }

    @Data
    public static class Delete {
        @NotNull(message = "Appointment ID is required")
        private Long id;
    }
}
