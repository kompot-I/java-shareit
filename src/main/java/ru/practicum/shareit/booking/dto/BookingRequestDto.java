package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.validators.DataRangeValidation;

import java.time.LocalDateTime;

@Data
@Builder
@DataRangeValidation
public class BookingRequestDto {

    @NotNull(message = "ID must be specified")
    private Long itemId;

    @NotNull(message = "Booking start date must be specified")
    @Future(message = "Date must be selected later than the current time")
    private LocalDateTime start;

    @NotNull(message = "Booking end date must be specified")
    @Future(message = "Date must be selected later than the current time")
    private LocalDateTime end;

}
