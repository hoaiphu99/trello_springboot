package com.lhoaiphu.springboottraining.repository;

import com.lhoaiphu.springboottraining.entity.ERole;
import com.lhoaiphu.springboottraining.entity.Role;
import org.springframework.context.annotation.Primary;

import java.util.Optional;

@Primary
public interface RoleRepository extends BaseRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
