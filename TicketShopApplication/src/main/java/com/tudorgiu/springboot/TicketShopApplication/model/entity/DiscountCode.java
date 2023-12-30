package com.tudorgiu.springboot.TicketShopApplication.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "discount_codes")
public class DiscountCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="ticket_id")
    private Ticket ticket;

    @Column(name = "code")
    private String code;

    @Column(name = "discount_procent")
    private int discountProcent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscountProcent() {
        return discountProcent;
    }

    public void setDiscountProcent(int discountProcent) {
        this.discountProcent = discountProcent;
    }
}
