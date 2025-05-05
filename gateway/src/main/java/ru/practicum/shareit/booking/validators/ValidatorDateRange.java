package ru.practicum.shareit.booking.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

public class ValidatorDateRange implements ConstraintValidator<DataRangeValidation, BookingRequestDto> {
    @Override
    public boolean isValid(BookingRequestDto entity, ConstraintValidatorContext constraintValidatorContext) {
        return entity.getStart().isBefore(entity.getEnd());
    }
}
