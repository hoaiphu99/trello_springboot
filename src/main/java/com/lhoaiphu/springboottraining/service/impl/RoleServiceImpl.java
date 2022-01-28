package com.lhoaiphu.springboottraining.service.impl;

import com.lhoaiphu.springboottraining.entity.ERole;
import com.lhoaiphu.springboottraining.entity.Role;
import com.lhoaiphu.springboottraining.repository.RoleRepository;
import com.lhoaiphu.springboottraining.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<Role> getRoleByName(ERole name) {
        return roleRepository.findByName(name);
    }
}
