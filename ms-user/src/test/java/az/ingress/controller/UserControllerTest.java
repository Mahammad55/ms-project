package az.ingress.controller;

import az.ingress.model.dto.request.LoginRequest;
import az.ingress.model.dto.request.RegisterRequest;
import az.ingress.model.dto.response.JwtResponse;
import az.ingress.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void register_Success() throws Exception {
        // arrange
        var request = new RegisterRequest();
        request.setEmail("ilyazovmehemmed@gmail.com");
        request.setName("Mahammad");
        request.setSurname("Ilyazov");
        request.setPassword("password");
        request.setRepeatPassword("password");

        doNothing().when(userService).register(request);

        // act
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.email").value("ilyazovmehemmed@gmail.com"))
                .andExpect(status().isCreated());

        // assert
        verify(userService, times(1)).register(request);
    }

    @Test
    public void register_WhenEmailIsBlank_BadRequest() throws Exception {
        // arrange
        var request = new RegisterRequest();
        request.setEmail("");
        request.setName("Mahammad");
        request.setSurname("Ilyazov");
        request.setPassword("password");
        request.setRepeatPassword("password");

        doNothing().when(userService).register(request);

        // act
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // assert
        verify(userService, never()).register(request);
    }

    @Test
    public void register_WhenEmailIsNotValid_BadRequest() throws Exception {
        // arrange
        var request = new RegisterRequest();
        request.setEmail("ilyazovmehemmedgmail");
        request.setName("Mahammad");
        request.setSurname("Ilyazov");
        request.setPassword("password");
        request.setRepeatPassword("password");

        doNothing().when(userService).register(request);

        // act
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // assert
        verify(userService, never()).register(request);
    }

    @Test
    public void login_Success() throws Exception {
        // arrange
        var response = new JwtResponse();
        response.setUsername("ilyazovmehemmed@gmail.com");
        response.setToken("mockToken");

        LoginRequest request = new LoginRequest();
        request.setEmail("ilyazovmehemmed@gmail.com");
        request.setPassword("password");

        when(userService.login(request)).thenReturn(response);

        // act
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("ilyazovmehemmed@gmail.com"))
                .andExpect(jsonPath("$.token").value("mockToken"));

        // assert
        verify(userService, times(1)).login(request);
    }
}