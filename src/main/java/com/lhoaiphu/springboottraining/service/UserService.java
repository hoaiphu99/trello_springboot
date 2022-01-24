package com.lhoaiphu.springboottraining.service;

import com.lhoaiphu.springboottraining.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<UserDTO> retrieveUsers();

    public UserDTO saveUser(UserDTO userDTO);

    public Boolean existUser(String username);

    public Optional<UserDTO> getUserByUsername(String username);

    public UserDTO updateUser(Long userId, UserDTO userDTO);

    public Boolean deleteUser(Long userId);
}
