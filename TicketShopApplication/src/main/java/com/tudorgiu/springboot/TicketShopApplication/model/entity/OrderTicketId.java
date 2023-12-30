package com.tudorgiu.springboot.TicketShopApplication.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderTicketId implements Serializable {

    private int order;

    private int ticket;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTicketId that = (OrderTicketId) o;
        return getOrder() == that.getOrder() && getTicket() == that.getTicket();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrder(), getTicket());
    }
}
