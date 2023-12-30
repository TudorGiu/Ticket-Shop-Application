package com.tudorgiu.springboot.TicketShopApplication.controller.dao;

import com.tudorgiu.springboot.TicketShopApplication.model.entity.OrderTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTicketRepository extends JpaRepository<OrderTicket, Integer> {
}
