package com.tudorgiu.springboot.TicketShopApplication.controller.actualcontroller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/")
    public String showHome() {

        return "home";
    }
}
