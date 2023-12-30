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

    public TicketController(TicketService ticketService, EventService eventService, DiscountCodeService discountCodeService) {
        this.ticketService = ticketService;
        this.eventService = eventService;
        this.discountCodeService = discountCodeService;
    }

    @GetMapping("/editTicketPage")
    public String showFormForTicketEdit(@RequestParam("eventId") int eventId, @RequestParam("ticketId") int ticketId, Model model){

        Ticket ticket = ticketService.findById(ticketId);

        model.addAttribute("ticket", ticket).addAttribute("eventId", eventId);

        return "edit-ticket-page";
    }

    @GetMapping("/delete")
    public String deleteTicket(@RequestParam("eventId") int eventId, @RequestParam("ticketId") int ticketId){
        ticketService.deleteById(ticketId);

        return "redirect:/events/?eventId=" + eventId;
    }

    @PostMapping("/save")
    public String saveTicket(@ModelAttribute("ticket") Ticket ticketFromForm, @RequestParam("eventId") int eventId) {

        Ticket oldTicket;
        try{ // ticket is edited
            oldTicket = ticketService.findById(ticketFromForm.getId());

            ticketFromForm.setEvent(oldTicket.getEvent());
            ticketFromForm.setDiscountCodes(oldTicket.getDiscountCodes());
            ticketFromForm.setOrderTickets(oldTicket.getOrderTickets());

        }catch (NoSuchElementException e){ // new ticket
            ticketFromForm.setEvent(eventService.findById(eventId));
            ticketFromForm.setActive(true);
        }

        ticketService.save(ticketFromForm);

        return "redirect:/events/?eventId=" + eventId;
    }

    @GetMapping("/addNewTicketPage")
    public String showFormForTicketCreation(Model model, @RequestParam("eventId") int eventId){

        Ticket ticket = new Ticket();

        model.addAttribute("ticket", ticket).addAttribute("eventId", eventId);

        return "edit-ticket-page";
    }

    @GetMapping("/editDiscountCodePage")
    public String showFormForDiscountCodeEdit(@RequestParam("ticketId") int ticketId, @RequestParam("discountCodeId") int discountCodeId, Model model, @RequestParam("eventId") int eventId){

        DiscountCode discountCode = discountCodeService.findById(discountCodeId);

        model.addAttribute("discountCode", discountCode).addAttribute("ticketId", ticketId).addAttribute("eventId", eventId);;

        return "edit-discount-code-page";
    }

    @GetMapping("/deleteDiscountCode")
    public String deleteDiscountCode(@RequestParam("discountCodeId") int discountCodeId,  HttpServletRequest request){
        discountCodeService.deleteById(discountCodeId);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/saveDiscountCode")
    public String saveDiscountCode(@ModelAttribute("discountCode") DiscountCode discountCodeFromForm,
                                   @RequestParam("ticketId") int ticketId, @RequestParam("eventId") int eventId) {

        discountCodeFromForm.setTicket(ticketService.findById(ticketId));

        discountCodeService.save(discountCodeFromForm);

        return "redirect:/tickets/editTicketPage?eventId=" + eventId + "&ticketId=" + ticketId;
    }

    @GetMapping("/addNewDiscountCodePage")
    public String showFormForDiscountCodeCreation(Model model, @RequestParam("ticketId") int ticketId, @RequestParam("eventId") int eventId){

        DiscountCode discountCode = new DiscountCode();

        model.addAttribute("discountCode", discountCode).addAttribute("ticketId", ticketId).addAttribute("eventId", eventId);

        return "edit-discount-code-page";
    }
}
