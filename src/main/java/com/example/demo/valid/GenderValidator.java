package com.example.demo.valid;

import com.example.demo.common.Const;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class GenderValidator implements ConstraintValidator<ValidGender, String> {
    private static final List<String> VALID_GENDERS = Arrays.asList(Const.STRING_GENDER_MAN, Const.STRING_GENDER_WOMAN);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || VALID_GENDERS.contains(value);
    }
}
