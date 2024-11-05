package com.hana.service.Response;

import com.hana.service.DAO.Entity.UserServiceEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class UserServiceResponse extends BaseResponse{
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class GetByUserId extends BaseResponse {
        private List<UserServiceDTO> services;

        public GetByUserId(HttpServletRequest request) {
            super(request);
        }
    }

    @Data
    public static class UserServiceDTO {
        private Long id;
        private String serviceName;
        private Integer duration;
        private BigDecimal price;
        private String status;
        private LocalDateTime createDate;
        private LocalDateTime updateDate;

        public static UserServiceDTO fromEntity(UserServiceEntity entity) {
            UserServiceDTO dto = new UserServiceDTO();
            dto.setId(entity.getId());
            dto.setServiceName(entity.getServiceName());
            dto.setDuration(entity.getDuration());
            dto.setPrice(entity.getPrice());
            dto.setStatus(entity.getStatus());
            dto.setCreateDate(entity.getCreateDate());
            dto.setUpdateDate(entity.getUpdateDate());
            return dto;
        }
    }
}


