package com.lhoaiphu.springboottraining.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhoaiphu.springboottraining.dto.UserDTO;
import com.lhoaiphu.springboottraining.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//@WebMvcTest(UserController.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private final List<UserDTO> users = new ArrayList<>();

    UserDTO u1 = new UserDTO("admin",  "admin@gmail.com", "023456");
    UserDTO u2 = new UserDTO("phu", "phu@gmail.com", "123456");
    UserDTO u3 = new UserDTO("thang",  "thang@gmail.com", "223456");

    @BeforeEach
    void setUp(){
        this.users.add(u1);
        this.users.add(u2);
        this.users.add(u3);

    }

    @Test
    public void getAllUserTest_success() throws Exception {
        when(userService.retrieveUsers()).thenReturn(this.users);
        this.mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(users.size())))
                .andExpect(jsonPath("$.data[2].username", is("thang")));

    }

    @Test
    public void getUserByUsernameTest_success() throws Exception {

        when(userService.getUserByUsername(u1.getUsername())).thenReturn(Optional.of(u1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/users/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.data.username", is("admin")));
    }

    @Test
    public void createUserTest_success() throws Exception {
        UserDTO user = new UserDTO("user",  "user@gmail.com", "023456");

        when(userService.saveUser(user)).thenReturn(user);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));
//                .andExpect(jsonPath("$.data.username", is("user")));
    }

    @Test
    public void updateUserTest_success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("userEdit");

        when(userService.getUserByUsername(u1.getUsername())).thenReturn(Optional.of(u1));
        when(userService.updateUser(userDTO.getUserId(), userDTO)).thenReturn(userDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/api/v1/users/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void deleteUserTest_success() throws Exception {
        when(userService.getUserByUsername(u1.getUsername())).thenReturn(Optional.of(u1));

        mockMvc.perform(MockMvcRequestBuilders
        .delete("/api/v1/users/user/1"))
                .andExpect(status().isOk());
    }
}
