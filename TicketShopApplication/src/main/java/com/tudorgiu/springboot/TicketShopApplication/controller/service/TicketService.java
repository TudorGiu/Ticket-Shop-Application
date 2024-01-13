package com.tudorgiu.springboot.TicketShopApplication.controller.service;

import com.tudorgiu.springboot.TicketShopApplication.controller.dao.EventRepository;
import com.tudorgiu.springboot.TicketShopApplication.controller.dao.TicketRepository;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.Event;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.Ticket;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class TicketService {
    private TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Retrieve a ticket by its ID.
     *
     * @param id The ID of the ticket to retrieve.
     * @return The Ticket object associated with the provided ID.
     * @throws NoSuchElementException If no ticket is found with the given ID.
     */
    public Ticket findById(int id){

        Optional<Ticket> returnedValue = ticketRepository.findById(id);
        if(returnedValue.isPresent())
            return returnedValue.get();
        else
            throw new NoSuchElementException("Ticket with id " + id + " not found.");
    }

    public List<Ticket> findAll(){
        return ticketRepository.findAll();
    }

    public void save(Ticket ticket){
        ticketRepository.save(ticket);
    }

    /**
     * Soft delete a ticket by setting its 'active' status to false.
     *
     * @param id The ID of the ticket to delete.
     * @throws NoSuchElementException If no ticket is found with the given ID.
     */
    public void deleteById(int id){
        Optional<Ticket> ticketInCause = ticketRepository.findById(id);

        if(ticketInCause.isPresent()){
            ticketInCause.get().setActive(false);
            ticketRepository.save(ticketInCause.get());
        }else throw new NoSuchElementException("Ticket with id " + id + " not found.");
    }
}
