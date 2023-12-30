package com.tudorgiu.springboot.TicketShopApplication.controller.dao;

import com.tudorgiu.springboot.TicketShopApplication.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
