package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserDto entity) {
        log.info("Create a user from the data {}", entity);
        return userClient.create(entity);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody UserDto entity) {
        log.info("Update a user from the data {} by ID {}", entity, id);
        return userClient.update(id, entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> remove(@PathVariable("id") Long id) {
        log.info("Remove a user by ID {}", id);
        return userClient.remove(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@PathVariable("id") Long id) {
        log.info("Get user by ID {}", id);
        return userClient.getUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> getItems() {
        log.info("Get all users");
        return userClient.getUsers();
    }
}
