package com.tudorgiu.springboot.TicketShopApplication.model.builder;

import com.tudorgiu.springboot.TicketShopApplication.model.entity.ChargeRequest;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.Order;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.OrderTicket;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface Builder {
    OrderBuilder withUser(User user);
    OrderBuilder withTotalPrice(float totalPrice);
    OrderBuilder withDate(LocalDateTime date);
    OrderBuilder withOrderTickets(List<OrderTicket> orderTickets);
    OrderBuilder addOrderTicket(OrderTicket orderTicket);
    Order build();
}
