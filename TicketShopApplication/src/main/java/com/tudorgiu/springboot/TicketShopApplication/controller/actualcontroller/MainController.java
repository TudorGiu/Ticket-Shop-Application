package com.tudorgiu.springboot.TicketShopApplication.controller.actualcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String showHome() {

        return "home";
    }
}
