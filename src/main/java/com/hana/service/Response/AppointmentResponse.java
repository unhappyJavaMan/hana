package com.hana.service.Response;

import com.hana.service.DAO.Entity.AppointmentEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class AppointmentResponse {

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class GetAll extends BaseResponse {
        private List<AppointmentDTO> appointments;

        public GetAll(HttpServletRequest request) {
            super(request);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class GetById extends BaseResponse {
        private AppointmentDTO appointment;

        public GetById(HttpServletRequest request) {
            super(request);
        }
    }

    @Data
    public static class AppointmentDTO {
        private Long id;
        private Long customerId;
        private String customerName;
        private Long memberId;
        private String memberName;
        private LocalDate appointmentDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private String status;
        private LocalDateTime createDate;
        private LocalDateTime updateDate;

        public static AppointmentDTO fromEntity(AppointmentEntity entity) {
            AppointmentDTO dto = new AppointmentDTO();
            dto.setId(entity.getId());
            dto.setCustomerId(entity.getCustomer().getId());
            dto.setCustomerName(entity.getCustomer().getName());
            dto.setMemberId(entity.getUser().getId());
            dto.setMemberName(entity.getUser().getNickName());
            dto.setAppointmentDate(entity.getAppointmentDate());
            dto.setStartTime(entity.getStartTime());
            dto.setEndTime(entity.getEndTime());
            dto.setStatus(entity.getStatus());
            dto.setCreateDate(entity.getCreateDate());
            dto.setUpdateDate(entity.getUpdateDate());
            return dto;
        }
    }
}
