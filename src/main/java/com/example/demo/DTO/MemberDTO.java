package com.example.demo.DTO;


import com.example.demo.common.Const;
import com.example.demo.common.Method;
import com.example.demo.entity.MemberPO;
import com.example.demo.valid.ValidGender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
public class MemberDTO {
    private static String REGEXP_GENDER = null;

    static {
        REGEXP_GENDER = Method.vaildRegexpString(Const.STRING_GENDER_MAN, Const.STRING_GENDER_WOMAN);
    }

    @Data
    public static class addMember {
        @NotNull(message = "name not null")
        private String name;
        @ValidGender(message = "gender only Man or Woman")
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

    @Data
    public static class updaeteMember {
        @NotNull(message = "id not null")
        private Long id;
        @NotNull(message = "name not null")
        private String name;
        @ValidGender(message = "gender only Man or Woman")
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
        @NotNull(message = "joinDate not null")
        private LocalDate joinDate;

        @NotNull(message = "status not null")
        private String status;
    }



}
