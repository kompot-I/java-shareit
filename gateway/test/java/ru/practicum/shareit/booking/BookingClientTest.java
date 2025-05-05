package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingClientTest {

    @Mock
    private RestTemplate restTemplate;

    private BookingClient bookingClient;

    private BookingRequestDto dto;

    @BeforeEach
    void setUp() {
        RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        when(builder.build()).thenReturn(restTemplate);
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);

        String serverUrl = "http://localhost:8080";
        bookingClient = new BookingClient(serverUrl, builder);

        dto = new BookingRequestDto();
        dto.setItemId(1L);
    }

    @Test
    void getBookingByID() {
        when(restTemplate.exchange(
                eq("/1"),
                eq(HttpMethod.GET),
                argThat(entity -> true),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());
        ResponseEntity<Object> actualResponse = bookingClient.getBookingByID(1L, 1L);
        assertEquals(ResponseEntity.ok().build(), actualResponse);
    }

    @Test
    void approveBooking() {
        when(restTemplate.exchange(
                eq("/1?approved=true"),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = bookingClient.approveBooking(1L, 1L, true);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createBooking() {
        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.POST),
                argThat(entity -> entity.getBody().equals(dto)),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = bookingClient.createBooking(1L, dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getBookingAll() {
        when(restTemplate.exchange(
                eq("?state={state}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(Map.of("state", "ALL"))
        )).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = bookingClient.getBookingAll(1L, BookingState.ALL);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
