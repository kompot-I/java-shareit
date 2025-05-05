package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    void createUserShouldReturnUserDto() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("USER");
        userDto.setEmail("USER@email.ru");

        when(userService.create(any())).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("USER"))
                .andExpect(jsonPath("$.email").value("USER@email.ru"));
    }

    @Test
    void createUserWithDuplicateEmailShouldThrowException() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("USER");
        userDto.setEmail("USER@email.ru");

        when(userService.create(any())).thenThrow(new DuplicateException("An exception has occurred"));

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("An exception has occurred"));

        verify(userService, times(1)).create(any());
    }

    @Test
    void updateUserShouldReturnUpdatedDto() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Updated Name");
        userDto.setEmail("UPDATED@email.ru");

        when(userService.update(eq(1L), any())).thenReturn(userDto);

        mockMvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("UPDATED@email.ru"));
    }

    @Test
    void getUserByIdShouldReturnUserDto() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("USER");
        userDto.setEmail("USER@email.ru");

        when(userService.getUser(1L)).thenReturn(userDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("USER"))
                .andExpect(jsonPath("$.email").value("USER@email.ru"));
    }

    @Test
    void getAllUsersShouldReturnList() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("USER");
        userDto.setEmail("USER@email.ru");

        when(userService.getItems()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("USER"))
                .andExpect(jsonPath("$[0].email").value("USER@email.ru"));
    }

    @Test
    void deleteUserShouldReturnOk() throws Exception {
        doNothing().when(userService).remove(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userService).remove(1L);
    }
}
