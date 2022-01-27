package com.lhoaiphu.springboottraining.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lhoaiphu.springboottraining.dto.LoginResponse;
import com.lhoaiphu.springboottraining.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"security.basic.enabled=false", "management.security.enabled=false"})
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private WebApplicationContext context;

    @LocalServerPort
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper();

    TestRestTemplate testRestTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

//    @Autowired
//    private Filter springSecurityFilterChain;

//    @Before
//    public void setup() {
//       mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
//        // mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilters(springSecurityFilterChain).build();
//
//    }

//    @WithMockUser("/admin")
    @Test
    public void retrieveUserTest() throws Exception {

        String token = "Bearer " + getAuthorization();

        headers.add("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/api/v1/users"),
                HttpMethod.GET,
                entity,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()).andReturn();
//
//        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void createUserTest() throws JsonProcessingException {
        UserDTO userDTO = new UserDTO("abcxyz", "abcxyz@gmail.com", "thien123");

        String token = "Bearer " + getAuthorization();

        headers.add("Authorization", token);
        HttpEntity<UserDTO> entity = new HttpEntity<UserDTO>(userDTO, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/api/v1/auth/signup"),
                HttpMethod.POST,
                entity,
                String.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

//        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
//
//        assertTrue(actual.contains("/api/v1/auth/signup"));
    }

    @Test
    public void getUserByUsernameTest() throws JSONException, JsonProcessingException {
        String token = "Bearer " + getAuthorization();

        headers.add("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/api/v1/users/thuan"),
                HttpMethod.GET,
                entity,
                String.class
        );

        String expected = "{\n" +
                "    \"status\": \"success\",\n" +
                "    \"data\": {\n" +
                "        \"userId\": 8,\n" +
                "        \"username\": \"thuan\",\n" +
                "        \"email\": \"thuan@gmail.com\",\n" +
                "        \"password\": \"$2a$10$Y2AmAhJhYu6x9ruaVRBvPeDYAWayxTco1GqWEYO9QKBycgweKRrEK\",\n" +
                "        \"roles\": [\n" +
                "            \"ROLE_USER\"\n" +
                "        ]\n" +
                "    },\n" +
                "    \"message\": \"User found!\"\n" +
                "}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void deleteUserTest() throws JsonProcessingException {
        String token = "Bearer " + getAuthorization();

        headers.add("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/api/v1/users/user/13"),
                HttpMethod.DELETE,
                entity,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void updateUserTest() throws JsonProcessingException {
        String token = "Bearer " + getAuthorization();

        headers.add("Authorization", token);

        UserDTO userDTO = new UserDTO("thiendum", "thiendum@gmail.com", "thien123");
        userDTO.setEmail("thienvy@gmail.com");

        HttpEntity<UserDTO> entity = new HttpEntity<UserDTO>(userDTO, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/api/v1/users/user/11"),
                HttpMethod.PUT,
                entity,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    private String getAuthorization() throws JsonProcessingException {

        ObjectNode loginRequest = objectMapper.createObjectNode();
        loginRequest.put("username", "admin");
        loginRequest.put("password", "admin123");

        HttpEntity<ObjectNode> authenticationEntity = new HttpEntity<ObjectNode>(loginRequest,
                headers);

        ResponseEntity<LoginResponse> authenticationResponse = testRestTemplate.exchange(
                createURLWithPort("/api/v1/auth/signin"),
                HttpMethod.POST, authenticationEntity, LoginResponse.class);

        return Objects.requireNonNull(authenticationResponse.getBody()).getToken();
    }
}