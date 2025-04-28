package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{id}")
    public BookingDto getBookingByID(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("id") Long bookingId) {
        return bookingService.getBookingByID(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingDto> getBookingAll(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(required = false, defaultValue = "ALL") BookingState state) {
        return bookingService.getBookingAll(userId, state);
    }

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid BookingRequestDto entity) {
        return bookingService.createBooking(userId, entity);
    }

    @PatchMapping("/{id}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("id") Long bookingId, @RequestParam Boolean approved) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getBookingAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(required = false, defaultValue = "ALL") BookingState state) {
        return bookingService.getBookingAllByOwner(userId, state);
    }
}
