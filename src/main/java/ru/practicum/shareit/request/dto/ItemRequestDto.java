package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ItemRequestDto {
    private Long id;
    private String description;
    private UserDto requestor;
    private LocalDateTime created;
}
