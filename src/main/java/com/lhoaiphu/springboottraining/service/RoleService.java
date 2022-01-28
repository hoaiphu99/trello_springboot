package com.lhoaiphu.springboottraining.service;

import com.lhoaiphu.springboottraining.entity.ERole;
import com.lhoaiphu.springboottraining.entity.Role;

import java.util.Optional;

public interface RoleService {
    public Optional<Role> getRoleByName(ERole name);
}
