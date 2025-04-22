package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "An available cannot be empty.")
    private Boolean available;
}
