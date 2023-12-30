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

    private EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    public List<Event> findAll(){
        return eventRepository.findAll();
    }

    public Event findById(int id){
        try {
            return eventRepository.findById(id).get();
        }catch (NoSuchElementException e){
            throw new NoSuchElementException("Event with id " + id + " not found.");
        }
    }

    public void save(Event newEvent) {
        eventRepository.save(newEvent);
    }

    public void deleteById(int id){
        Optional<Event> eventInCause = eventRepository.findById(id);

        if(eventInCause.isPresent()){
            eventInCause.get().setActive(false);
            eventRepository.save(eventInCause.get());
        }else throw new NoSuchElementException("Event with id " + id + " not found.");
    }
}
