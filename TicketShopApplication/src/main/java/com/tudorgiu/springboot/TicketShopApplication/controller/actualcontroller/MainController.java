package com.tudorgiu.springboot.TicketShopApplication.controller.actualcontroller;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.tudorgiu.springboot.TicketShopApplication.controller.service.*;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class MainController {

    private EventService eventService;
    private BasketService basketService;
    private OrderService orderService;
    private UserService userService;

    @Autowired
    private StripeService paymentsService;

    @Autowired
    private OrderTicketService orderTicketService;

    @Autowired
    private TicketService ticketService;

    public MainController(EventService eventService, BasketService basketService, OrderService orderService, UserService userService) {
        this.eventService = eventService;
        this.basketService = basketService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {

        List<Event> events = eventService.findAll();

        int basketItemCount = basketService.getBasketItemCount();

        model.addAttribute("events", events).addAttribute("basketItemCount", basketItemCount);
        return "home";
    }

    @GetMapping("/orders")
    public String showOrdersPage(Model model){

        List<Order> orders = orderService.findAll();

        model.addAttribute("orders", orders);

        return "orders-page";
    }
    @PostMapping("/charge")
    public String charge(ChargeRequest chargeRequest, Model model) throws StripeException {

        // payment part
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(ChargeRequest.Currency.RON);
        Charge charge = paymentsService.charge(chargeRequest);

        // order part
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String activeUserEmail = null;

        if(authentication.getPrincipal() instanceof User) {
            activeUserEmail = ((User) authentication.getPrincipal()).getUsername();

            Order newOrder = new Order();
            newOrder.setUser(userService.getByEmail(activeUserEmail));
            newOrder.setDate(LocalDateTime.now());
            newOrder.setTotalPrice(basketService.getTotalPriceDiscounted());

            orderService.save(newOrder);

            for(Ticket t : basketService.getTicketsAsList()){
                OrderTicket ot = new OrderTicket();
                ot.setTicket(t);
                ot.setAmount(basketService.getTickets().get(t));
                ot.setOrder(newOrder);
                orderTicketService.save(ot);

                // reducing each of the ticket's amount
                t.setAmount(t.getAmount()-basketService.getTickets().get(t));
                ticketService.save(t);
            }


            // emptying the basket
            basketService.getTickets().clear();
            basketService.getAddedDiscountCodes().clear();
        }

        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
        return "payment-end-page";
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "payment-end-page";
    }

}
