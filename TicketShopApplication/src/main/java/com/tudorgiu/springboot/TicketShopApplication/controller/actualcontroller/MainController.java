package com.tudorgiu.springboot.TicketShopApplication.controller.actualcontroller;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.tudorgiu.springboot.TicketShopApplication.controller.service.*;
import com.tudorgiu.springboot.TicketShopApplication.model.builder.OrderBuilder;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    // Constructor injection of services
    public MainController(EventService eventService, BasketService basketService, OrderService orderService, UserService userService) {
        this.eventService = eventService;
        this.basketService = basketService;
        this.orderService = orderService;
        this.userService = userService;
    }

    // Show the home page with a list of events and basket item count
    @GetMapping("/")
    public String showHomePage(Model model) {

        List<Event> events = eventService.findAll();

        int basketItemCount = basketService.getBasketItemCount();

        model.addAttribute("events", events).addAttribute("basketItemCount", basketItemCount);
        return "home";
    }

    // Show the orders page with a list of all orders
    @GetMapping("/orders")
    public String showOrdersPage(Model model){

        List<Order> orders = orderService.findAll();

        model.addAttribute("orders", orders);

        return "orders-page";
    }

    // Handle the charge (payment) process
    @PostMapping("/charge")
    public String charge(ChargeRequest chargeRequest, Model model) throws StripeException {

        // Payment part
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(ChargeRequest.Currency.RON);
        Charge charge = paymentsService.charge(chargeRequest);

        // Order part
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String activeUserEmail = null;

        // Get the active user's email
        if(authentication.getPrincipal() instanceof User) {
            activeUserEmail = ((User) authentication.getPrincipal()).getUsername();

            // Create a new order
            Order newOrder = new OrderBuilder()
                    .withUser(userService.getByEmail(activeUserEmail))
                    .withDate(LocalDateTime.now())
                    .withTotalPrice(basketService.getTotalPriceDiscounted())
                    .build();

            orderService.save(newOrder);

            // Iterate through tickets in the basket and create order tickets
            for(Ticket t : basketService.getTicketsAsList()){
                OrderTicket ot = new OrderTicket();
                ot.setTicket(t);
                ot.setAmount(basketService.getTickets().get(t));
                ot.setOrder(newOrder);
                orderTicketService.save(ot);

                // Reduce each ticket's amount
                t.setAmount(t.getAmount()-basketService.getTickets().get(t));
                ticketService.save(t);
            }


            // Empty the basket and added discount codes
            basketService.getTickets().clear();
            basketService.getAddedDiscountCodes().clear();
        }

        // Add charge details to the model
        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
        return "payment-end-page";
    }

    // Handle StripeException errors
    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "payment-end-page";
    }

}
