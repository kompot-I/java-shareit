package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.RequestDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RequestControllerTest {

    @Mock
    private RequestClient requestClient;

    @InjectMocks
    private RequestController requestController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(requestController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createRequest() throws Exception {
        RequestDto dto = new RequestDto();
        dto.setDescription("TEXT");

        when(requestClient.create(anyLong(), any(RequestDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(
                post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk());
    }

    @Test
    void getRequest() throws Exception {
        mockMvc.perform(
                get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
        ).andExpect(status().isOk());
    }

    @Test
    void getMyRequests() throws Exception {
        mockMvc.perform(
                get("/requests")
                        .header("X-Sharer-User-Id", 1L)
        ).andExpect(status().isOk());
    }

    @Test
    void getAllRequests() throws Exception {
        mockMvc.perform(
                get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
        ).andExpect(status().isOk());
    }

}
