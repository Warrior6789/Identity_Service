package com.philong.identity_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DobValidator.class}) // liên kết với class validator
public @interface DobConstraint {
    String message() default "Invalid date of birth";

    int min(); // đây lờ thuộc tính đc định nghĩa

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}


//@Target là annotation này đc dùng ở đâu vd: field,method...
//@Retention là annotation sẽ xử lý lúc nào, vd: runtime,...
//@Constraint