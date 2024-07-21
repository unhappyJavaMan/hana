package com.example.demo.valid;

import com.example.demo.common.Const;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class MemberStatusValidator implements ConstraintValidator<ValidGender, String> {
    private static final List<String> VALID_STATUS = Arrays.asList(Const.STRING_MEMBER_STATUS_ACTIVE
            , Const.STRING_MEMBER_STATUS_SUSPENDED, Const.STRING_MEMBER_STATUS_DELETED);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || VALID_STATUS.contains(value);
    }
}
