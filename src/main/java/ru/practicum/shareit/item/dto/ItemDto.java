package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(message = "An name cannot be empty.")
    private String name;
    @NotBlank(message = "An description cannot be empty.")
    private String description;
    @NotNull(message = "An available cannot be empty.")
    private Boolean available;
}
