package com.lhoaiphu.springboottraining.repository;

import com.lhoaiphu.springboottraining.entity.User;
import org.springframework.context.annotation.Primary;

import java.util.Optional;

@Primary
public interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);


}
