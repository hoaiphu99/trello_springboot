package com.lhoaiphu.springboottraining.repository.impl;

import com.lhoaiphu.springboottraining.entity.User;
import com.lhoaiphu.springboottraining.repository.UserRepository;

import javax.persistence.EntityManager;
import java.util.Optional;

public class UserRepositoryImpl extends BaseRepositoryImpl<User, Long> implements UserRepository {
    public UserRepositoryImpl(EntityManager em) {
        super(User.class, em);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return
                Optional.ofNullable(
                jpaQueryFactory
                        .select(user)
                        .from(user)
                        .where(user.username.equalsIgnoreCase(username))
                        .fetchFirst()
        );
    }

    @Override
    public Boolean existsByUsername(String username) {
        return
                jpaQueryFactory
                .from(user)
                .where(user.username.equalsIgnoreCase(username))
                .fetchFirst() != null;
    }
}
