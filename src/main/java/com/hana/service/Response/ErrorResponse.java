package com.hana.service.Response;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@Data
public class ErrorResponse extends BaseResponse{

    public ErrorResponse(HttpServletRequest request, String message) {
        super(request);
        this.message = message;
    }

    private String message;

}
