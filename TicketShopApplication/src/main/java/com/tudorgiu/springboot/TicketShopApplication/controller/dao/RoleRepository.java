package com.tudorgiu.springboot.TicketShopApplication.controller.dao;

import com.tudorgiu.springboot.TicketShopApplication.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
