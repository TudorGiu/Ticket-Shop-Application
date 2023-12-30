package com.tudorgiu.springboot.TicketShopApplication.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "total_price")
    private float totalPrice;

    @Column(name = "date")
    private LocalDateTime date;

    @OneToMany(mappedBy = "order")
    private List<OrderTicket> orderTickets = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<OrderTicket> getOrderTickets() {
        return orderTickets;
    }

    public void setOrderTickets(List<OrderTicket> orderTickets) {
        this.orderTickets = orderTickets;
    }
}
