package com.tudorgiu.springboot.TicketShopApplication.dao;

import com.tudorgiu.springboot.TicketShopApplication.model.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface DAO<T> {
    void save(T t);

    T findById(Integer id);

    List<T> findAll();
}
