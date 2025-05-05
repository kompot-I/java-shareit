package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Collection;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto createBooking(Long userId, BookingRequestDto entity) {
        User user = getUser(userId);
        Item item = getItem(entity.getItemId());
        if (!item.getAvailable()) {
            throw new ValidationException("Item with id " + entity.getItemId() + " unavailable for booking");
        }
        if (item.getOwner().equals(user.getId())) {
            throw new NotFoundException("The owner cannot book his item");
        }
        Booking booking = BookingMapper.toBookingModel(entity);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        booking = bookingRepository.save(booking);
        return BookingMapper.toDto(booking);
    }

    @Override
    public BookingDto approveBooking(Long userId, Long bookingId, boolean approved) {
        getUser(userId);
        Booking booking = getBooking(bookingId);
        if (!booking.getItem().getOwner().equals(userId)) {
            throw new NotFoundException("User with id " + userId + " is not the owner of the item " + booking.getItem().getId());
        }
        BookingStatus status = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(status);
        booking = bookingRepository.save(booking);
        return BookingMapper.toDto(booking);
    }

    @Override
    public BookingDto getBookingByID(Long userId, Long bookingId) {
        User user = getUser(userId);
        Booking booking = getBooking(bookingId);
        if (!booking.getBooker().getId().equals(user.getId()) && !booking.getItem().getOwner().equals(user.getId())) {
            throw new NotFoundException("Couldn't find the corresponding booking entry");
        }
        return BookingMapper.toDto(booking);
    }

    @Override
    public Collection<BookingDto> getBookingAll(Long userId, BookingState state) {
        User user = getUser(userId);
        Collection<Booking> bookings = switch (state) {
            case CURRENT -> bookingRepository.findAllByBookerCurrent(user, LocalDateTime.now());
            case PAST -> bookingRepository.findAllByBookerPast(user, LocalDateTime.now());
            case FUTURE -> bookingRepository.findAllByBookerFuture(user, LocalDateTime.now());
            case WAITING -> bookingRepository.findAllByBookerStatus(user, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findAllByBookerStatus(user, BookingStatus.REJECTED);
            default -> bookingRepository.findAllByBooker(user);
        };
        return BookingMapper.toDto(bookings);
    }

    @Override
    public Collection<BookingDto> getBookingAllByOwner(Long userId, BookingState state) {
        User user = getUser(userId);
        Collection<Booking> bookings = switch (state) {
            case CURRENT -> bookingRepository.findAllByOwnerCurrent(user, LocalDateTime.now());
            case PAST -> bookingRepository.findAllByOwnerPast(user, LocalDateTime.now());
            case FUTURE -> bookingRepository.findAllByOwnerFuture(user, LocalDateTime.now());
            case WAITING -> bookingRepository.findAllByOwnerStatus(user, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findAllByOwnerStatus(user, BookingStatus.REJECTED);
            default -> bookingRepository.findAllByOwner(user);
        };
        return BookingMapper.toDto(bookings);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AccessDeniedException("User with id " + userId + " not found"));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
    }

    private Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));
    }
}
