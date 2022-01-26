package com.lhoaiphu.springboottraining.dto;

import com.lhoaiphu.springboottraining.entity.User;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class    UserDTO {
    private Long userId;

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private Set<String> roles;

    public UserDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserDTO(String username, String email, String password, Set<String> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());

        Set<String> roles = new HashSet<>();
        user.getRoles().forEach(r -> {
            roles.add(r.getName().name());
        });
        userDTO.setRoles(roles);

        return userDTO;

    }

    public User convertToEntity(UserDTO userDTO) {
        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        return user;
    }

    public List<UserDTO> userDTOList(List<User> userList) {
        List<UserDTO> dtoList = new ArrayList<>();

        userList.forEach(u -> {
            dtoList.add(convertToDTO(u));
        });

        return dtoList;
    }

}
