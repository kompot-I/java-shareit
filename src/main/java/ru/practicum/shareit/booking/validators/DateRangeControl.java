package ru.practicum.shareit.booking.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidatorDateRange.class)
public @interface DateRangeControl {
    String message() default "Incorrect date range detected";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
