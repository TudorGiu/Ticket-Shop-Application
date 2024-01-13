package com.tudorgiu.springboot.TicketShopApplication.controller.service;

import com.tudorgiu.springboot.TicketShopApplication.model.entity.DiscountCode;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.Ticket;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.text.DecimalFormat;
import java.util.*;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class BasketService {
    // Map to store tickets and their quantities in the basket
    private Map<Ticket, Integer> tickets = new HashMap<>();

    // List to store added discount codes
    private List<DiscountCode> addedDiscountCodes = new ArrayList<>();

    public List<DiscountCode> getAddedDiscountCodes() {
        return addedDiscountCodes;
    }

    public Map<Ticket, Integer> getTickets() {
        return tickets;
    }

    // Method to add a ticket to the basket
    public void addTicket(Ticket ticket){
        boolean exists = false;
        for (Ticket pivot : tickets.keySet()) {
            if(pivot.getId() == ticket.getId()) { // the tickets are the same, but the reference might not
                int quantity = tickets.get(pivot);
                tickets.put(pivot, quantity+1);
                exists = true;
                break;
            }
        }

        if(exists == false){
            tickets.put(ticket, 1);
        }
    }

    // Method to remove a ticket from the basket
    public void removeTicket(Ticket ticket){
        boolean exists = false;
        for (Ticket pivot : tickets.keySet()) {
            if(pivot.getId() == ticket.getId()) { // the tickets are the same, but the reference might not
                exists = true;
                int quantity = tickets.get(pivot);
                if(quantity >= 2){
                    tickets.put(pivot, quantity-1);
                } else if (quantity == 1) {
                    tickets.remove(pivot);
                }
                break;
            }
        }

        if(exists == false){
            throw new NoSuchElementException("Basket does not have a ticket with id " + ticket.getId());
        }
    }

    // Method to get the total count of items in the basket
    public int getBasketItemCount(){
        int itemCount = 0;

        for (Integer quantity : tickets.values()) {
            itemCount += quantity;
        }

        return itemCount;
    }

    // Method to calculate the total price without discounts
    public float getTotalPriceNoDiscount(){
        float sum = 0;
        for(Ticket pivot : this.tickets.keySet()){
            sum += pivot.getPrice() * tickets.get(pivot);
        }
        return  sum;
    }

    // Method to calculate the total price with applied discounts
    public float getTotalPriceDiscounted(){

        float totalPrice = this.getTotalPriceNoDiscount();
        for(DiscountCode discountCodePivot: addedDiscountCodes)
            for(Ticket ticketPivot: this.tickets.keySet())
                if(discountCodePivot.getTicket().getId() == ticketPivot.getId())
                    totalPrice -= ((float) discountCodePivot.getDiscountProcent())/100 * ticketPivot.getPrice() * this.tickets.get(ticketPivot);

        return totalPrice;
    }

    // Method to convert the basket tickets to a list
    public List<Ticket> getTicketsAsList(){
        return new ArrayList<>(tickets.keySet());
    }
}
