package com.tudorgiu.springboot.TicketShopApplication.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="event_id")
    private Event event;

    @Column(name="ticket_type_name")
    private String name;

    @Column(name="price")
    private float price;

    @OneToMany(mappedBy = "ticket")
    private List<DiscountCode> discountCodes;

    @OneToMany(mappedBy = "ticket")
    private List<OrderTicket> orderTickets = new ArrayList<>();

    @Column(name = "amount")
    private int amount;

    @Column(name = "is_active")
    private boolean isActive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<DiscountCode> getDiscountCodes() {
        return discountCodes;
    }

    public void setDiscountCodes(List<DiscountCode> discountCodes) {
        this.discountCodes = discountCodes;
    }

    public List<OrderTicket> getOrderTickets() {
        return orderTickets;
    }

    public void setOrderTickets(List<OrderTicket> orderTickets) {
        this.orderTickets = orderTickets;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", event=" + event +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", discountCodes=" + discountCodes +
                ", orderTickets=" + orderTickets +
                ", amount=" + amount +
                ", isActive=" + isActive +
                '}';
    }
}
