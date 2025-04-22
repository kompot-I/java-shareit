package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        User userModel = userStorage.create(UserMapper.toModel(entity));
        return UserMapper.toDto(userModel);
    }

    @Override
    public UserDto update(Long id, UserDto entity) {
        User user = userStorage.update(id, UserMapper.toModel(entity));
        return UserMapper.toDto(user);
    }

    @Override
    public void remove(Long id) {
        userStorage.remove(id);
    }

    @Override
    public UserDto getItem(Long id) {
        User user = userStorage.getItem(id);
        return UserMapper.toDto(user);
    }

    @Override
    public Collection<UserDto> getItems() {
        return userStorage.getItems().stream()
                .map(UserMapper::toDto)
                .toList();
    }
}
