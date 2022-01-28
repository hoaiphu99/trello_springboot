package com.lhoaiphu.springboottraining.repository;

import com.lhoaiphu.springboottraining.entity.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);


}
