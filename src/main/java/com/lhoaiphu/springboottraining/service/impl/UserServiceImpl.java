package com.lhoaiphu.springboottraining.service.impl;

import com.lhoaiphu.springboottraining.dto.UserDTO;
import com.lhoaiphu.springboottraining.entity.ERole;
import com.lhoaiphu.springboottraining.entity.Role;
import com.lhoaiphu.springboottraining.entity.User;
import com.lhoaiphu.springboottraining.exception.ResourceNotFoundEx;
import com.lhoaiphu.springboottraining.repository.RoleRepo;
import com.lhoaiphu.springboottraining.repository.UserRepo;
import com.lhoaiphu.springboottraining.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final PasswordEncoder encoder;

    public UserServiceImpl(RoleRepo roleRepo, UserRepo userRepo, PasswordEncoder encoder) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public List<UserDTO> retrieveUsers() {
        List<User> users = userRepo.findAll();

        return new UserDTO().userDTOList(users);
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) throws ResourceNotFoundEx {
        User user = new UserDTO().convertToEntity(userDTO);

        user = new User(user.getUsername(), encoder.encode(user.getPassword()), user.getEmail());
        Set<String> strRoles = userDTO.getRoles();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null) {
            Role role = roleRepo.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new ResourceNotFoundEx("Error: Role is not found"));
            roles.add(role);
        } else {
            strRoles.forEach(r -> {
                if ("admin".equals(r.toLowerCase())) {
                    Role role = null;
                    try {
                        role = roleRepo.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new ResourceNotFoundEx("Error: Role is not found"));
                    } catch (ResourceNotFoundEx resourceNotFoundEx) {
                        resourceNotFoundEx.printStackTrace();
                    }
                    roles.add(role);
                }
                Role role = null;
                try {
                    role = roleRepo.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new ResourceNotFoundEx("Error: Role is not found"));
                } catch (ResourceNotFoundEx resourceNotFoundEx) {
                    resourceNotFoundEx.printStackTrace();
                }
                roles.add(role);
            });
        }

        user.setRoles(roles);

        return new UserDTO().convertToDTO(userRepo.save(user));
    }

    @Override
    public Boolean existUser(String username) {
        return userRepo.existsByUsername(username);
    }

    @Override
    public Optional<UserDTO> getUserByUsername(String username) throws ResourceNotFoundEx {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundEx("User not found for this username: " + username));
        return Optional.of(new UserDTO().convertToDTO(user));
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) throws ResourceNotFoundEx {
        User existUser = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundEx("User not found for this id: " + userId));

        existUser.setUsername(userDTO.getUsername());
        existUser.setEmail(userDTO.getEmail());
        existUser.setPassword(encoder.encode(userDTO.getPassword()));

        User user = new User();
        user = userRepo.save(existUser);

        return new UserDTO().convertToDTO(user);
    }

    @Override
    public Boolean deleteUser(Long userId) throws ResourceNotFoundEx {
        userRepo.findById(userId).orElseThrow(
                () -> new ResourceNotFoundEx("User not found for this id: " + userId)
        );

        userRepo.deleteById(userId);
        return true;
    }
}
