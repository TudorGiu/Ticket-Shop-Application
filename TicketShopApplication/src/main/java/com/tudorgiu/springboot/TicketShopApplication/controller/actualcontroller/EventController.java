package com.tudorgiu.springboot.TicketShopApplication.controller.actualcontroller;

import com.tudorgiu.springboot.TicketShopApplication.controller.service.BasketService;
import com.tudorgiu.springboot.TicketShopApplication.controller.service.EventService;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.Event;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.User;
import org.hibernate.engine.jdbc.batch.spi.BatchKey;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;
import java.io.IOException;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/events")
public class EventController {

    private EventService eventService;
    private BasketService basketService;

    // Constructor injection of services
    public EventController(EventService eventService, BasketService basketService) {
        this.eventService = eventService;
        this.basketService = basketService;
    }

    // Display the form for editing an event
    @GetMapping("/editEventPage")
    public String showFormForEventEdit(@RequestParam("eventId") int eventId, Model model){

        // Retrieve the event by ID
        Event event = eventService.findById(eventId);

        model.addAttribute("event", event);

        return "edit-event-page";
    }

    // Save or update an event
    @PostMapping("/save")
    public String saveEvent(@ModelAttribute("event") Event eventFromForm, @RequestParam("image") MultipartFile image) throws IOException {

        // Check if the image is empty and retain the existing picture if applicable
        if(image.isEmpty()){
            if(eventFromForm.getId() != 0){
                Event oldEvent = eventService.findById(eventFromForm.getId());
                eventFromForm.setPicture(oldEvent.getPicture());
            }
        }
        else{
            // Set the event picture with the contents of the uploaded image
            eventFromForm.setPicture(image.getBytes());
        }

        // Set the event as active and save/update it
        eventFromForm.setActive(true);
        eventService.save(eventFromForm);


        return "redirect:/";
    }


    // Delete an event
    @GetMapping("/delete")
    public String deleteEvent(@RequestParam("eventId") int id){
        // Delete the event by ID
        eventService.deleteById(id);

        return "redirect:/";
    }

    // Display the form for creating a new event
    @GetMapping("/addNewEventPage")
    public String showFormForEventCreation(Model model){

        Event event = new Event();

        model.addAttribute("event", event);

        return "edit-event-page";
    }

    // View details of an event
    @GetMapping("/")
    public String viewEventPage(@RequestParam("eventId") int eventId, Model model){

        Event event = eventService.findById(eventId);

        int basketItemCount = basketService.getBasketItemCount();

        // Add the event and basket item count to the model
        model.addAttribute("event", event).addAttribute("basketItemCount", basketItemCount);

        return "event-page";
    }


    // Exception handler for NoSuchElementException
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchElementFoundException(NoSuchElementException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}
