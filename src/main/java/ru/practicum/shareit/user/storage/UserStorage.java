package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    User update(Long id, User user);

    void remove(Long id);

    Optional<User> getUser(Long userId);

    Collection<User> getUsers();

    Optional<User> findByEmail(String email);
}
