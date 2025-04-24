package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDate;


@Data
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDate start;
    private LocalDate end;
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;
}
