package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    void createBookingWithUnknownUser() throws Exception {
        Long userId = 10L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(start);
        bookingRequestDto.setEnd(end);

        when(bookingService.createBooking(eq(userId), any()))
                .thenThrow(new NotFoundException("Object not found"));

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Object not found"));
    }

    @Test
    void createBookingWithUnknownItem() throws Exception {
        Long userId = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(10L);
        bookingRequestDto.setStart(start);
        bookingRequestDto.setEnd(end);

        when(bookingService.createBooking(eq(userId), any()))
                .thenThrow(new NotFoundException("Object not found"));

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Object not found"));
    }

    @Test
    void createBookingWithUnavailableItem() throws Exception {
        Long userId = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(start);
        bookingRequestDto.setEnd(end);

        when(bookingService.createBooking(eq(userId), any()))
                .thenThrow(new ValidationException("Error of Validation"));

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Error of Validation"));
    }

    @Test
    void approveBooking() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;

        BookingDto dto = new BookingDto();
        dto.setId(bookingId);

        when(bookingService.approveBooking(eq(userId), eq(bookingId), eq(true)))
                .thenReturn(dto);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .patch("/bookings/{id}?approved=true", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId));
    }

    @Test
    void getBookingByID() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;

        BookingDto dto = new BookingDto();
        dto.setId(bookingId);

        when(bookingService.getBookingByID(eq(userId), eq(bookingId)))
                .thenReturn(dto);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .get("/bookings/{id}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId));
    }

    @Test
    void getBookingAll() throws Exception {
        Long userId = 1L;

        when(bookingService.getBookingAll(eq(userId), eq(BookingState.ALL)))
                .thenReturn(List.of());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .get("/bookings")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingAllByOwner() throws Exception {
        Long userId = 1L;

        when(bookingService.getBookingAllByOwner(eq(userId), eq(BookingState.ALL)))
                .thenReturn(List.of());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }
}
