package com.lhoaiphu.springboottraining.controller;

import com.lhoaiphu.springboottraining.dto.ResponseDTO;
import com.lhoaiphu.springboottraining.dto.UserDTO;
import com.lhoaiphu.springboottraining.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<ResponseDTO> getAllUsers() {
        ResponseDTO response = new ResponseDTO();

        List<ResponseDTO> responseDTOS = new ArrayList<>();

        try {
            List<UserDTO> userDTOS = userService.retrieveUsers();
            List list = Collections.synchronizedList(new ArrayList<>(userDTOS));

            if(responseDTOS.addAll(list)) {
                response.setData(userDTOS);
            }
            response.setMessage("Get all success");
            response.setStatus("success");
        } catch (Exception e) {
            log.info("Get all user failed");
            throw new RuntimeException("Get all failed: " + e.getMessage());
        }
        log.info("Get all user success");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ResponseDTO> findUserByUsername(@PathVariable("username") String username) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            Optional<UserDTO> userDTO = userService.getUserByUsername(username);

            responseDTO.setData(userDTO);
            responseDTO.setMessage("User found!");
            responseDTO.setStatus("success");

        } catch (Exception e) {
            log.info("Cannot found user with username {}", username);
            throw new RuntimeException("Cannot find user: " + e.getMessage());
        }
        log.info("Found user with username {}", username);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PutMapping("/user/{user_id}")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable("user_id") Long id, @RequestBody UserDTO userDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            UserDTO dto = userService.updateUser(id, userDTO);
            responseDTO.setData(dto);
            responseDTO.setMessage("user updated");
            responseDTO.setStatus("success");
        } catch (Exception e) {
            throw new RuntimeException("Cannot update user: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @DeleteMapping("/user/{user_id}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable("user_id") Long id) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            this.userService.deleteUser(id);
            responseDTO.setData("");
            responseDTO.setMessage("user deleted");
            responseDTO.setStatus("success");
        } catch (Exception e) {
            throw new RuntimeException("Cannot delete user: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
