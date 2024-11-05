package com.hana.service.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

public class UserServiceRequest {
    @Data
    public static class Create {
        @NotBlank(message = "服務名稱不能為空")
        @Size(max = 100, message = "服務名稱最多100字元")
        private String serviceName;

        @NotNull(message = "服務時間不能為空")
        @Min(value = 1, message = "服務時間必須大於0分鐘")
        private Integer duration;

        @NotNull(message = "服務價錢不能為空")
        @DecimalMin(value = "0.0", message = "服務價錢必須大於等於0")
        private BigDecimal price;
    }

    @Data
    public static class Update extends Create {
        @NotNull(message = "id不能為空")
        private Long id;
    }

    @Data
    public static class Delete {
        @NotNull(message = "id不能為空")
        private Long id;
    }
}