package com.tudorgiu.springboot.TicketShopApplication.controller.actualcontroller;

import com.tudorgiu.springboot.TicketShopApplication.controller.service.BasketService;
import com.tudorgiu.springboot.TicketShopApplication.controller.service.DiscountCodeService;
import com.tudorgiu.springboot.TicketShopApplication.controller.service.TicketService;
import com.tudorgiu.springboot.TicketShopApplication.controller.service.UserService;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.DiscountCode;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.Ticket;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/basket")
public class BasketController {
    @Value("${stripe.public.key}")
    private String stripePublicKey;
    private BasketService basketService;
    private TicketService ticketService;
    private DiscountCodeService discountCodeService;
    private UserService userService;

    public BasketController(BasketService basketService, TicketService ticketService, DiscountCodeService discountCodeService, UserService userService) {
        this.basketService = basketService;
        this.ticketService = ticketService;
        this.discountCodeService = discountCodeService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String showBasketPage(Model model, @RequestParam Optional<String> exceptionMsg){

        if(exceptionMsg.isPresent())
            model.addAttribute("exceptionMsg", exceptionMsg.get());

        Map<Ticket, Integer> basketContent = basketService.getTickets();

        float totalPrice = basketService.getTotalPriceNoDiscount();

        model.addAttribute("basketContent", basketContent).addAttribute("totalPrice", totalPrice);

        return "basket-page";
    }

    @GetMapping("/addItem")
    public String addItemToBasket(@RequestParam("ticketId") int id, HttpServletRequest request ){
        basketService.addTicket(ticketService.findById(id));

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    @GetMapping("/removeItem")
    public String removeItemFromBasket(@RequestParam("ticketId") int id, HttpServletRequest request ){
        basketService.removeTicket(ticketService.findById(id));

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    @GetMapping("/order")
    public String viewOrder(Model model){

        // check if there are enough tickets in the database
        for(Map.Entry<Ticket, Integer> entry : basketService.getTickets().entrySet()){
            if(ticketService.findById(entry.getKey().getId()).getAmount() < entry.getValue())
            {
                String notEnoughTicketsExceptionMsg = "Wanted to order " + entry.getValue() +
                        " tickets of type " + ticketService.findById(entry.getKey().getId()).getName() +
                        ", but in the database there are only " +
                        ticketService.findById(entry.getKey().getId()).getAmount() + ".";
                return "redirect:/basket/?exceptionMsg=" + notEnoughTicketsExceptionMsg;
            }
        }

        int roundedTotalPriceInBani = Math.round(basketService.getTotalPriceDiscounted()*100);

        model.addAttribute("stripePublicKey", stripePublicKey)
                .addAttribute("basketContent", basketService.getTickets())
                .addAttribute("totalPrice", roundedTotalPriceInBani)
                .addAttribute("addedDiscountCodes", basketService.getAddedDiscountCodes());

        return "order-page";
    }

    @PostMapping("/applyDiscount")
    public RedirectView applyDiscount(@RequestParam("discountCodeText") String discountCodeText){

        List<DiscountCode> discountCodes = discountCodeService.findByCode(discountCodeText);

        boolean alreadyAdded = false;

        for(DiscountCode discountCodePivot : basketService.getAddedDiscountCodes())
            if(discountCodePivot.getCode().equals(discountCodeText)){
                alreadyAdded = true;
                break;
            }

        if(alreadyAdded == false)
            for(DiscountCode discountCodePivot: discountCodes)
                for(Ticket ticketPivot: basketService.getTicketsAsList())
                    if(discountCodePivot.getTicket().getId() == ticketPivot.getId())
                        basketService.getAddedDiscountCodes().add(discountCodePivot);

        return new RedirectView("/basket/order");
    }
}
