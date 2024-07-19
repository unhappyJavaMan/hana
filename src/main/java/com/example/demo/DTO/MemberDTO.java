package com.example.demo.DTO;


import com.example.demo.common.Const;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

public class MemberDTO {
    @Data
    public static class addMember {
        @NotNull(message = "name not null")
        private String name;
        @Pattern(regexp = "(^"+ Const.STRING_GENDER_MAN +"$|^"+Const.STRING_GENDER_WOMAN+"$)", message = "gender only Man or Woman")
        @NotNull(message = "gender not null")
        private String gender;
        @NotNull(message = "email not null")
        @Email
        private String email;
        @NotNull(message = "phoneNumber not null")
        @Pattern(regexp = "\\d{10}")
        private String phoneNumber;
        @NotNull(message = "birthDate not null")
        private LocalDate birthDate;
    }
}
