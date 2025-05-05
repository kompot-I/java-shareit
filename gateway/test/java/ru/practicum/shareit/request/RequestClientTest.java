package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestClientTest {

    @Mock
    private RestTemplate restTemplate;

    private RequestClient requestClient;

    private RequestDto dto;

    @BeforeEach
    void setUp() {
        RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        when(builder.build()).thenReturn(restTemplate);
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);

        String serverUrl = "http://localhost:8080";
        requestClient = new RequestClient(serverUrl, builder);

        dto = new RequestDto();
        dto.setId(1L);
    }

    @Test
    void createRequest() {
        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = requestClient.create(1L, dto);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void getBookingByID() {
        when(restTemplate.exchange(
                eq("/1"),
                eq(HttpMethod.GET),
                argThat(entity -> true),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());
        ResponseEntity<Object> actualResponse = requestClient.getRequest(1L, 1L);
        assertEquals(ResponseEntity.ok().build(), actualResponse);
    }

    @Test
    void getMyRequests() {
        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = requestClient.getMyRequests(1L);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void getAllRequests() {
        when(restTemplate.exchange(
                eq("/all"),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = requestClient.getAllRequests(1L);
        assertEquals(ResponseEntity.ok().build(), response);
    }
}
