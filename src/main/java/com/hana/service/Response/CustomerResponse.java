package com.hana.service.Response;

import com.hana.service.DAO.Entity.CustomerEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

public class CustomerResponse {

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class GetAll extends BaseResponse {
        private List<CustomerDTO> customers;

        public GetAll(HttpServletRequest request) {
            super(request);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class GetById extends BaseResponse {
        private CustomerDTO customer;

        public GetById(HttpServletRequest request) {
            super(request);
        }
    }

    @Data
    public static class CustomerDTO {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private String status;
        private LocalDateTime createDate;
        private LocalDateTime updateDate;

        public static CustomerDTO fromEntity(CustomerEntity entity) {
            CustomerDTO dto = new CustomerDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setEmail(entity.getEmail());
            dto.setPhone(entity.getPhone());
            dto.setStatus(entity.getStatus());
            dto.setCreateDate(entity.getCreateDate());
            dto.setUpdateDate(entity.getUpdateDate());
            return dto;
        }
    }
}
