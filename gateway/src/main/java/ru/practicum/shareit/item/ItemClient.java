package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addComment(long userId, Long itemId, CommentDto entity) {
        return post("/" + itemId + "/comment", userId, entity);
    }

    public ResponseEntity<Object> create(long userId, ItemDto entity) {
        return post("", userId, entity);
    }

    public ResponseEntity<Object> update(long userId, Long itemId, ItemDto entity) {
        return patch("/" + itemId, userId, entity);
    }

    public ResponseEntity<Object> getItem(long userId, Long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItems(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getSearch(long userId, String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", userId, parameters);
    }

}
