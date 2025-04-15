package com.philong.identity_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private int min;

    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {

        if(Objects.isNull(localDate))
        return true;

        long year = ChronoUnit.YEARS.between(localDate, LocalDate.now());

        return year >= min;
    }
}

// void initialize(A constraintAnnotation)
// mục đích đc gọi 1 lần khi instance của ConstraintValidator đc tạo ra bởi bean validation
// nó cho phép validator của b nhận và lưu trữ các thuộc tính của annotation mà nó xử lý
//