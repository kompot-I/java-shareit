package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookingMapper {

    public static BookingDto toDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setItem(ItemMapper.toDto(booking.getItem()));
        dto.setBooker(UserMapper.toDto(booking.getBooker()));
        dto.setStatus(booking.getStatus());
        return dto;
    }

    public static List<BookingDto> toDto(Collection<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Booking toBookingModel(BookingRequestDto dto) {
        Booking booking = new Booking();
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        return booking;
    }

}
