package com.tudorgiu.springboot.TicketShopApplication.controller.service;

import com.tudorgiu.springboot.TicketShopApplication.controller.dao.EventRepository;
import com.tudorgiu.springboot.TicketShopApplication.controller.dao.OrderRepository;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.Event;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.Order;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAll(){
        return orderRepository.findAll();
    }

    public void save(Order newOrder) {
        orderRepository.save(newOrder);
    }
}
