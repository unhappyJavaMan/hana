package com.hana.service.Response;

import com.hana.service.DAO.Entity.UserEntity;
import com.hana.service.Utils.TimeUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

public class UserResponse extends BaseResponse {
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class GetAll extends BaseResponse {
        private List<UserDTO> users;

        public GetAll(HttpServletRequest request) {
            super(request);
        }
    }


    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class GetById extends BaseResponse {
        private UserDTO user;

        public GetById(HttpServletRequest request) {
            super(request);
        }
    }

    @Data
    public static class UserDTO {
        private Long id;
        private String account;
        private String nickName;
        private String email;
        private String phone;
        private String status;
        private String createDate;
        private String updateDate;
        private List<RoleDTO> roleIds;

        public static UserDTO fromEntity(UserEntity entity) {
            UserDTO dto = new UserDTO();
            dto.setId(entity.getId());
            dto.setAccount(entity.getAccount());
            dto.setNickName(entity.getNickName());
            dto.setEmail(entity.getEmail());
            dto.setPhone(entity.getPhone());
            dto.setStatus(entity.getStatus());
            dto.setCreateDate(TimeUtils.localDateTimeToTimeStamp(entity.getCreateDate()));
            dto.setUpdateDate(TimeUtils.localDateTimeToTimeStamp(entity.getUpdateDate()));
            return dto;
        }

    }
    @Data
    public static class RoleDTO {
        private Long id;
        private String name;
    }
}
