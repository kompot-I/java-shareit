package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestItemDto {
    private Long id;
    private Long owner;
    @NotBlank(message = "The name of the item cannot be empty.")
    private String name;
}
