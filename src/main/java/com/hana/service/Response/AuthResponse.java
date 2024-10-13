package com.hana.service.Response;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

public class AuthResponse {
    @Data
    public static class LoginResponse extends BaseResponse {
        private String loginToken;

        public LoginResponse(HttpServletRequest request) {
            super(request);
        }
    }

}
