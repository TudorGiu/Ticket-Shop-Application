package com.tudorgiu.springboot.TicketShopApplication.controller.service;

import com.tudorgiu.springboot.TicketShopApplication.controller.dao.EventRepository;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.Event;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.Ticket;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class EventService {

    // Repository for interacting with the Event entity in the database
    private EventRepository eventRepository;

    // Constructor for Dependency Injection
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Retrieve all events from the repository
    public List<Event> findAll(){
        return eventRepository.findAll();
    }

    public Event findById(int id){
        try {
            // Attempt to find the event by id in the repository
            return eventRepository.findById(id).get();
        }catch (NoSuchElementException e){
            // Throw an exception if the event is not found
            throw new NoSuchElementException("Event with id " + id + " not found.");
        }
    }

    // Save a new event or update an existing one in the repository
    public void save(Event newEvent) {
        eventRepository.save(newEvent);
    }

    // Soft delete an event by marking it as inactive
    public void deleteById(int id){
        Optional<Event> eventInCause = eventRepository.findById(id);

        if(eventInCause.isPresent()){
            // Set the event as inactive and save it to the repository
            eventInCause.get().setActive(false);
            eventRepository.save(eventInCause.get());
        }else{
            // Throw an exception if the event is not found
            throw new NoSuchElementException("Event with id " + id + " not found.");
        }

    }
}
