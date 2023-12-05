package com.tudorgiu.springboot.TicketShopApplication.dao;

import com.tudorgiu.springboot.TicketShopApplication.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO implements DAO<User>{

    private EntityManager entityManager;

    @Autowired
    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public User findById(Integer id){
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> findAll() {
        TypedQuery<User> typedQuery = entityManager.createQuery("FROM User order by lastName asc", User.class);

        return typedQuery.getResultList();
    }

    public List<User> findByLastName(String lastName){
        TypedQuery<User> typedQuery = entityManager.createQuery("FROM User WHERE lastName=:lastNamePlaceholder", User.class);
        typedQuery.setParameter("lastNamePlaceholder", lastName);

        return typedQuery.getResultList();
    }
}



