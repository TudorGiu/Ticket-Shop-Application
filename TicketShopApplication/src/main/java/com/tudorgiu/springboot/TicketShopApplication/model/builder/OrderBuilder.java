package com.tudorgiu.springboot.TicketShopApplication.model.builder;

import com.tudorgiu.springboot.TicketShopApplication.model.entity.Order;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.OrderTicket;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class OrderBuilder implements Builder {

    private final Order order;

    public OrderBuilder() {
        this.order = new Order();
    }

    public OrderBuilder withUser(User user) {
        order.setUser(user);
        return this;
    }

    public OrderBuilder withTotalPrice(float totalPrice) {
        order.setTotalPrice(totalPrice);
        return this;
    }

    public OrderBuilder withDate(LocalDateTime date) {
        order.setDate(date);
        return this;
    }

    public OrderBuilder withOrderTickets(List<OrderTicket> orderTickets) {
        order.setOrderTickets(orderTickets);
        return this;
    }

    public OrderBuilder addOrderTicket(OrderTicket orderTicket) {
        order.getOrderTickets().add(orderTicket);
        return this;
    }

    public Order build() {
        return order;
    }
}