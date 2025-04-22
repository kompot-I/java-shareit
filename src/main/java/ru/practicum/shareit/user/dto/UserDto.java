package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "An name cannot be empty.")
    private String name;

    @NotBlank(message = "An email cannot be empty.")
    @Email(message = "The email must contain the @ symbol.")
    private String email;
}
