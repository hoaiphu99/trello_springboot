package com.lhoaiphu.springboottraining.service;

import com.lhoaiphu.springboottraining.dto.UserDTO;
import com.lhoaiphu.springboottraining.exception.ResourceNotFoundEx;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<UserDTO> retrieveUsers();

    public UserDTO saveUser(UserDTO userDTO) throws ResourceNotFoundEx;

    public Boolean existUser(String username);

    public Optional<UserDTO> getUserByUsername(String username) throws ResourceNotFoundEx;

    public UserDTO updateUser(Long userId, UserDTO userDTO) throws ResourceNotFoundEx;

    public Boolean deleteUser(Long userId) throws ResourceNotFoundEx;
}
