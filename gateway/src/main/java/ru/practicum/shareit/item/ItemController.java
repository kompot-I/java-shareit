package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.CommentDto;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemDto entity) {
        log.info("Create an item from the data {}, User {}", entity, userId);
        return itemClient.create(userId, entity);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("id") Long itemId, @RequestBody ItemDto entity) {
        log.info("Update item from data {} on ID {}, User {}", entity, itemId, userId);
        return itemClient.update(userId, itemId, entity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("id") Long itemId) {
        log.info("Get item by ID {}, User {}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get items, User {}", userId);
        return itemClient.getItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getSearch(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        log.info("To search for a item in the text {}, User {}", text, userId);
        return itemClient.getSearch(userId, text);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("id") Long itemId, @RequestBody @Valid CommentDto entity) {
        log.info("Create comment from data {} for Item {}, User {}", entity, itemId, userId);
        return itemClient.addComment(userId, itemId, entity);
    }
}
