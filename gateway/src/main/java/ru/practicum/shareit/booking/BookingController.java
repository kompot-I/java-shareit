package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

@RequiredArgsConstructor
@Controller
@Slf4j
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBookingByID(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("id") Long bookingId) {
        log.info("Get a booking by ID {}, User {}", bookingId, userId);
        return bookingClient.getBookingByID(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingAll(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(required = false, defaultValue = "ALL") BookingState state) {
        log.info("Get all bookings by status {}, User {}", state, userId);
        return bookingClient.getBookingAll(userId, state);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid BookingRequestDto entity) {
        log.info("Create a booking from the data {}, User {}", entity, userId);
        return bookingClient.createBooking(userId, entity);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("id") Long bookingId, @RequestParam Boolean approved) {
        log.info("Confirm booking by ID {}, User {}", bookingId, userId);
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(required = false, defaultValue = "ALL") BookingState state) {
        log.info("Get the owner's reservations by status {}, User {}", state, userId);
        return bookingClient.getBookingAllByOwner(userId, state);
    }
}
