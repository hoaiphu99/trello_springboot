package com.lhoaiphu.springboottraining.service;

import com.lhoaiphu.springboottraining.dto.UserDTO;
import com.lhoaiphu.springboottraining.entity.ERole;
import com.lhoaiphu.springboottraining.entity.Role;
import com.lhoaiphu.springboottraining.entity.User;
import com.lhoaiphu.springboottraining.exception.ResourceNotFoundEx;
import com.lhoaiphu.springboottraining.repository.RoleRepo;
import com.lhoaiphu.springboottraining.repository.UserRepo;
import com.lhoaiphu.springboottraining.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    @Mock
    UserRepo userRepo;

    @Mock
    RoleRepo roleRepo;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    PasswordEncoder encoder;

    @Test
    public void whenGetAll_shouldReturnList() {

        List<User> users = new ArrayList<>();
        users.add(new User("admin",  "admin@gmail.com", "023456"));
        users.add(new User("phu", "phu@gmail.com", "123456"));
        users.add(new User("thang",  "thang@gmail.com", "223456"));

        when(userRepo.findAll()).thenReturn(users);

        List<UserDTO> userDTOS = userService.retrieveUsers();

        assertEquals("admin", userDTOS.get(0).getUsername());
        assertEquals(users.size(), userDTOS.size());

        verify(userRepo, atLeastOnce()).findAll();
    }

    @Test
    public void whenGetAll_shouldReturnEmptyList() {
        when(userRepo.findAll()).thenReturn(Collections.emptyList());

        List<UserDTO> userDTOS = userService.retrieveUsers();

        assertEquals(userDTOS.size(), Collections.emptyList().size());
    }

    @Test
    public void whenGivenUsername_shouldReturnUser() throws ResourceNotFoundEx {
        Optional<User> user = Optional.of(new User("admin",  encoder.encode("023456"), "admin@gmail.com"));

        when(userRepo.findByUsername("admin")).thenReturn(user);

        Optional<UserDTO> userDTO = userService.getUserByUsername("admin");

        assertEquals("admin", userDTO.get().getUsername());
        verify(userRepo, atLeastOnce()).findByUsername("admin");
    }

    @Test
    public void whenGiven_shouldAdd() throws ResourceNotFoundEx {
        Set<Role> roles = new HashSet<>();
        Set<String> rolesDTO = new HashSet<>();

        UserDTO userDTO = new UserDTO("admin",  "admin@gmail.com", encoder.encode("023456"), rolesDTO);

        User user = new User("admin",  encoder.encode("023456"), "admin@gmail.com", roles);

        when(userRepo.save(any())).thenReturn(user);

        UserDTO dto = userService.saveUser(userDTO);

        assertEquals("admin", dto.getUsername());

        verify(userRepo, atLeastOnce()).save(any());
    }

    @Test
    public void whenGivenId_shouldUpdate() throws ResourceNotFoundEx {
        UserDTO userDTO = new UserDTO("admin",  "admin@gmail.com", "023456");
        Optional<User> user = Optional.of(new User("admin",  "023456", "admin@gmail.com"));

        when(userRepo.findById(1L)).thenReturn(user);
        when(userRepo.save(any(User.class))).thenReturn(new User("admin",  "023456", "admin@gmail.com"));

        UserDTO userDTO1 = userService.updateUser(1L, userDTO);

        assertEquals("admin", userDTO1.getUsername());
        verify(userRepo, atLeastOnce()).findById(1L);
        verify(userRepo, atLeastOnce()).save(any(User.class));
    }

    @Test
    public void whenGivenId_shouldDelete() throws ResourceNotFoundEx {
        Optional<User> user = Optional.of(new User("admin",  "023456", "admin@gmail.com"));

        when(userRepo.findById(1L)).thenReturn(user);

        Boolean result = userService.deleteUser(1L);

        assertEquals(result, true);
        verify(userRepo, atLeastOnce()).deleteById(1L);
    }
}
