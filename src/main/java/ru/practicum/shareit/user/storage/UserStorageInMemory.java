package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserStorageInMemory implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User entity) {
        entity.setId(getNextId());
        users.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public User update(Long id, User entity) {
        User oldUser = users.get(id);

        if (entity.getEmail() != null && !entity.getEmail().isBlank()) {
            oldUser.setEmail(entity.getEmail());
        }
        if (entity.getName() != null && !entity.getName().isBlank()) {
            oldUser.setName(entity.getName());
        }
        return oldUser;
    }

    @Override
    public void remove(Long id) {
        users.remove(id);
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    private long getNextId() {

        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }
}
