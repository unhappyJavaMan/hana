package com.hana.service.Common;

import com.hana.service.Response.BaseResponse;
import com.hana.service.Utils.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    LogUtils logger = new LogUtils();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        logger.error(ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        BaseResponse response = new BaseResponse(request) {
            private Map<String, String> errorDetails = errors;

            public Map<String, String> getErrorDetails() {
                return errorDetails;
            }
        };
        response.setTransactionInvoice(request.getAttribute(Const.REQUEST_ID).toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
        logger.error(ex);
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        BaseResponse response = new BaseResponse(request) {
            private Map<String, String> errorDetails = errorResponse;

            public Map<String, String> getErrorDetails() {
                return errorDetails;
            }
        };
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
