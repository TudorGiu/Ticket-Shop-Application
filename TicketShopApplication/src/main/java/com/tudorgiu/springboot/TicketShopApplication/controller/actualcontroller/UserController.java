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

    // Constructor injection of UserService
    public UserController(UserService userService){
        this.userService = userService;
    }

    // Show the login page
    @GetMapping("/loginPage")
    public String showLoginPage(){

        return "login-page";
    }

    // Show the register page
    @GetMapping("/registerPage")
    public String showRegisterPage(Model model){

        // Create a new User object
        User theUser = new User();

        // Add the user to the model
        model.addAttribute("user", theUser);

        return "register-page";
    }

    // Save a new user
    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User theNewUser){

        // Save the new user
        userService.save(theNewUser);

        return "redirect:/";
    }

    // Handle NoSuchElementException errors
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchElementFoundException(NoSuchElementException exception) {
        // Return a response entity with NOT_FOUND status and exception message
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}
