package com.lhoaiphu.springboottraining.controller;

import com.lhoaiphu.springboottraining.dto.ResponseDTO;
import com.lhoaiphu.springboottraining.dto.UserDTO;
import com.lhoaiphu.springboottraining.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserService userService;

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
            throw new RuntimeException("Get all failed: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/user")
    public ResponseEntity<ResponseDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            if(userService.existUser(userDTO.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseDTO("failed", "", "Username already taken!"));
            }
            UserDTO dto = userService.saveUser(userDTO);
            responseDTO.setData(dto);
            responseDTO.setMessage("New user created");
            responseDTO.setStatus("success");
        } catch (Exception e) {
            throw new RuntimeException("Create failed: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
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
            throw new RuntimeException("Cannot find user: " + e.getMessage());
        }

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
