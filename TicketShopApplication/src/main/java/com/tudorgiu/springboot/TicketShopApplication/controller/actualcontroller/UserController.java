package com.tudorgiu.springboot.TicketShopApplication.controller.actualcontroller;

import com.tudorgiu.springboot.TicketShopApplication.controller.service.UserService;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/loginPage")
    public String showLoginPage(){

        return "login-page";
    }

    @GetMapping("/registerPage")
    public String showRegisterPage(Model model){

        User theUser = new User();

        model.addAttribute("user", theUser);

        return "register-page";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User theNewUser){

        userService.save(theNewUser);

        return "redirect:/";
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchElementFoundException(NoSuchElementException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}
