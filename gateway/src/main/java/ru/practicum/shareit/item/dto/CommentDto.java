package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank(message = "The text of the comment cannot be empty")
    private String text;
    private String authorName;
    private LocalDateTime created;
}