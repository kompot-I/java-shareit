package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemClientTest {

    @Mock
    private RestTemplate restTemplate;

    private ItemClient itemClient;

    private ItemDto dto;

    @BeforeEach
    void setUp() {
        RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        when(builder.build()).thenReturn(restTemplate);
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);

        String serverUrl = "http://localhost:8080";
        itemClient = new ItemClient(serverUrl, builder);

        dto = new ItemDto();
        dto.setId(1L);
    }

    @Test
    void getItemByID() {
        when(restTemplate.exchange(
                eq("/1"),
                eq(HttpMethod.GET),
                argThat(entity -> true),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());
        ResponseEntity<Object> actualResponse = itemClient.getItem(1L, 1L);
        assertEquals(ResponseEntity.ok().build(), actualResponse);
    }

    @Test
    void addCommentWithParams() {
        CommentDto commentDto = new CommentDto();
        when(restTemplate.exchange(
                eq("/1/comment"),
                eq(HttpMethod.POST),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = itemClient.addComment(1L, 1L, commentDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createItem() {
        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = itemClient.create(1L, dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateItem() {
        when(restTemplate.exchange(
                eq("/1"),
                eq(HttpMethod.PATCH),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = itemClient.update(1L, 1L, dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllItems() {
        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = itemClient.getItems(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getSearch() {
        when(restTemplate.exchange(
                eq("/search?text={text}"),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class),
                eq(Map.of("text", "item"))
        )).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = itemClient.getSearch(1L, "item");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
