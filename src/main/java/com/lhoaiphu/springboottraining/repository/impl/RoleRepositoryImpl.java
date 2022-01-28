package com.lhoaiphu.springboottraining.repository.impl;

import com.lhoaiphu.springboottraining.entity.ERole;
import com.lhoaiphu.springboottraining.entity.Role;
import com.lhoaiphu.springboottraining.repository.RoleRepository;

import javax.persistence.EntityManager;
import java.util.Optional;

public class RoleRepositoryImpl extends BaseRepositoryImpl<Role, Long> implements RoleRepository {
    public RoleRepositoryImpl(EntityManager em) {
        super(Role.class, em);
    }

    @Override
    public Optional<Role> findByName(ERole name) {
        return Optional.of(jpaQueryFactory
                .select(role)
                .from(role)
                .where(role.name.eq(name))
                .fetchFirst());
    }
}
