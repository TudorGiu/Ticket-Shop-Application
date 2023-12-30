package com.tudorgiu.springboot.TicketShopApplication.controller.service;

import com.tudorgiu.springboot.TicketShopApplication.controller.dao.OrderTicketRepository;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.OrderTicket;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderTicketService {

    private OrderTicketRepository orderTicketRepository;

    public OrderTicketService(OrderTicketRepository orderTicketRepository) {
        this.orderTicketRepository = orderTicketRepository;
    }

    public void save(OrderTicket orderTicket){
        orderTicketRepository.save(orderTicket);
    }
}
