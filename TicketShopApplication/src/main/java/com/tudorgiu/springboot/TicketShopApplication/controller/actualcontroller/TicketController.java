package com.tudorgiu.springboot.TicketShopApplication.controller.actualcontroller;

import com.tudorgiu.springboot.TicketShopApplication.controller.service.DiscountCodeService;
import com.tudorgiu.springboot.TicketShopApplication.controller.service.EventService;
import com.tudorgiu.springboot.TicketShopApplication.controller.service.TicketService;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.DiscountCode;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.Event;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.Ticket;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private TicketService ticketService;

    private EventService eventService;

    private DiscountCodeService discountCodeService;

    // Constructor injection of services
    public TicketController(TicketService ticketService, EventService eventService, DiscountCodeService discountCodeService) {
        this.ticketService = ticketService;
        this.eventService = eventService;
        this.discountCodeService = discountCodeService;
    }

    // Show the form for editing a ticket
    @GetMapping("/editTicketPage")
    public String showFormForTicketEdit(@RequestParam("eventId") int eventId, @RequestParam("ticketId") int ticketId, Model model){

        Ticket ticket = ticketService.findById(ticketId);

        model.addAttribute("ticket", ticket).addAttribute("eventId", eventId);

        return "edit-ticket-page";
    }

    // Delete a ticket
    @GetMapping("/delete")
    public String deleteTicket(@RequestParam("eventId") int eventId, @RequestParam("ticketId") int ticketId){

        // Delete the ticket by ID
        ticketService.deleteById(ticketId);

        return "redirect:/events/?eventId=" + eventId;
    }

    // Save or update a ticket
    @PostMapping("/save")
    public String saveTicket(@ModelAttribute("ticket") Ticket ticketFromForm, @RequestParam("eventId") int eventId) {

        // Retrieve the old ticket if it exists
        Ticket oldTicket;
        try{ // Ticket is edited
            oldTicket = ticketService.findById(ticketFromForm.getId());

            // Preserve associations with event, discount codes, and order tickets
            ticketFromForm.setEvent(oldTicket.getEvent());
            ticketFromForm.setDiscountCodes(oldTicket.getDiscountCodes());
            ticketFromForm.setOrderTickets(oldTicket.getOrderTickets());

        }catch (NoSuchElementException e){ // new ticket
            // Set the event for the new ticket and mark it as active
            ticketFromForm.setEvent(eventService.findById(eventId));
            ticketFromForm.setActive(true);
        }

        // Save or update the ticket
        ticketService.save(ticketFromForm);

        return "redirect:/events/?eventId=" + eventId;
    }

    // Show the form for creating a new ticket
    @GetMapping("/addNewTicketPage")
    public String showFormForTicketCreation(Model model, @RequestParam("eventId") int eventId){

        // Create a new Ticket object
        Ticket ticket = new Ticket();

        model.addAttribute("ticket", ticket).addAttribute("eventId", eventId);

        return "edit-ticket-page";
    }

    // Show the form for editing a discount code
    @GetMapping("/editDiscountCodePage")
    public String showFormForDiscountCodeEdit(@RequestParam("ticketId") int ticketId, @RequestParam("discountCodeId") int discountCodeId, Model model, @RequestParam("eventId") int eventId){

        // Retrieve the discount code by ID
        DiscountCode discountCode = discountCodeService.findById(discountCodeId);

        model.addAttribute("discountCode", discountCode).addAttribute("ticketId", ticketId).addAttribute("eventId", eventId);;

        return "edit-discount-code-page";
    }

    // Delete a discount code
    @GetMapping("/deleteDiscountCode")
    public String deleteDiscountCode(@RequestParam("discountCodeId") int discountCodeId,  HttpServletRequest request){
        discountCodeService.deleteById(discountCodeId);

        // Get the referer (previous page) from the request and redirect
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    // Save or update a discount code
    @PostMapping("/saveDiscountCode")
    public String saveDiscountCode(@ModelAttribute("discountCode") DiscountCode discountCodeFromForm,
                                   @RequestParam("ticketId") int ticketId, @RequestParam("eventId") int eventId) {

        // Set the associated ticket for the discount code
        discountCodeFromForm.setTicket(ticketService.findById(ticketId));

        discountCodeService.save(discountCodeFromForm);

        return "redirect:/tickets/editTicketPage?eventId=" + eventId + "&ticketId=" + ticketId;
    }

    // Show the form for creating a new discount code
    @GetMapping("/addNewDiscountCodePage")
    public String showFormForDiscountCodeCreation(Model model, @RequestParam("ticketId") int ticketId, @RequestParam("eventId") int eventId){

        // Create a new DiscountCode object
        DiscountCode discountCode = new DiscountCode();

        // Add the discount code, ticketId, and eventId to the model
        model.addAttribute("discountCode", discountCode).addAttribute("ticketId", ticketId).addAttribute("eventId", eventId);

        return "edit-discount-code-page";
    }
}
