package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@NoArgsConstructor
public class RequestDto {
    private Long id;
    private String requestorName;
    private LocalDateTime created;
    private Collection<RequestItemDto> items;
    @NotBlank(message = "The description of the item request cannot be empty")
    private String description;
}
