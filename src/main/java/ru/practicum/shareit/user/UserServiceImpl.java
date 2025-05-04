package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto entity) {
        User userModel = UserMapper.toModel(entity);
        if (userRepository.findByEmail(userModel.getEmail()).isPresent()) {
            throw new DuplicateException("User with mail " + userModel.getEmail() + " already exist");
        }
        User createdUser = userRepository.save(userModel);
        return UserMapper.toDto(createdUser);
    }

    @Override
    public UserDto update(Long id, UserDto entity) {
        User user = getUserById(id);
        if (entity.getName() != null)
            user.setName(entity.getName());

        if (entity.getEmail() != null && !entity.getEmail().isBlank()) {
            checkDuplicateEmail(entity.getEmail(), id);
            user.setEmail(entity.getEmail());
        }

        userRepository.save(user);
        return UserMapper.toDto(user);
    }

    @Override
    public void remove(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    public UserDto getUser(Long id) {
        User user = getUserById(id);
        return UserMapper.toDto(user);
    }

    @Override
    public Collection<UserDto> getItems() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

    private void checkDuplicateEmail(String email, Long excludeId) {
        userRepository.findByEmail(email)
                .filter(user -> !user.getId().equals(excludeId))
                .ifPresent(user -> {
                    throw new DuplicateException("User with mail " + email + " already exist");
                });
    }
}
