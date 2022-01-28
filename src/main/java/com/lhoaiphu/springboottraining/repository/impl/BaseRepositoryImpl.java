package com.lhoaiphu.springboottraining.repository.impl;



import com.lhoaiphu.springboottraining.entity.QRole;
import com.lhoaiphu.springboottraining.entity.QUser;
import com.lhoaiphu.springboottraining.repository.BaseRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

public abstract class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    EntityManager em;
    JPAQueryFactory jpaQueryFactory;

    protected final QUser user = QUser.user;
    protected final QRole role = QRole.role;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.em = em;
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }


    @Override
    public T findByIdMandatory(ID id) throws IllegalArgumentException {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entity not found with id: " + id));
    }
}
