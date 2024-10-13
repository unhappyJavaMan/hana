package com.hana.service.Response;

import com.hana.service.Common.Const;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BaseResponse {

    public BaseResponse(HttpServletRequest request) {
        this.transactionInvoice = request.getAttribute(Const.REQUEST_ID).toString();
    }

    public BaseResponse(HttpServletRequest request, String message) {
        this.transactionInvoice = request.getAttribute(Const.REQUEST_ID).toString();
        this.message = message;
    }

    private String version = Const.BACKEND_VERSION_CODE;
    private String dateTimeUTC = String.valueOf(System.currentTimeMillis());
    private String transactionInvoice;
    private String message;

}

