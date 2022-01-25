package com.lhoaiphu.springboottraining.controller;

import com.lhoaiphu.springboottraining.dto.LoginRequest;
import com.lhoaiphu.springboottraining.dto.LoginResponse;
import com.lhoaiphu.springboottraining.dto.ResponseDTO;
import com.lhoaiphu.springboottraining.dto.UserDTO;
import com.lhoaiphu.springboottraining.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lhoaiphu.springboottraining.security.jwt.JwtUtils;
import com.lhoaiphu.springboottraining.security.service.UserDetailsImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(JwtUtils jwtUtils, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(r -> r.getAuthority())
                .collect(Collectors.toList());
        log.info("Login success");
        return ResponseEntity.status(HttpStatus.OK).body(
                new LoginResponse(
                        jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles
                )
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
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
            log.info("Register failed");
            throw new RuntimeException("Create failed: " + e.getMessage());
        }
        log.info("Register success");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

}
