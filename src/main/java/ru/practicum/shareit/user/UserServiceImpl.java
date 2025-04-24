package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto create(UserDto entity) {
        User userModel = UserMapper.toModel(entity);
        if (userStorage.findByEmail(userModel.getEmail()).isPresent()) {
            throw new DuplicateException("User with mail " + userModel.getEmail() + " already exist");
        }
        User createdUser = userStorage.create(userModel);
        return UserMapper.toDto(createdUser);
    }

    @Override
    public UserDto update(Long id, UserDto entity) {
        userStorage.getUser(id);
        User incomingUser = UserMapper.toModel(entity);
        if (incomingUser.getEmail() != null && !incomingUser.getEmail().isBlank()) {
            checkDuplicateEmail(incomingUser.getEmail(), id);
        }

        User updatedUser = userStorage.update(id, incomingUser);
        return UserMapper.toDto(updatedUser);
    }

    @Override
    public void remove(Long id) {
        userStorage.remove(id);
    }

    @Override
    public UserDto getUser(Long id) {
        User user = userStorage.getUser(id);
        return UserMapper.toDto(user);
    }

    @Override
    public Collection<UserDto> getItems() {
        return userStorage.getUsers().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    private void checkDuplicateEmail(String email, Long excludeId) {
        userStorage.findByEmail(email)
                .filter(user -> !user.getId().equals(excludeId))
                .ifPresent(user -> {
                    throw new DuplicateException("User with mail " + email + " already exist");
                });
    }
}
