package com.example.spring.boot.hibernate.security.springboothibernatesecurity.controller;

import com.example.spring.boot.hibernate.security.springboothibernatesecurity.domain.User;
import com.example.spring.boot.hibernate.security.springboothibernatesecurity.dto.UserRegistrationDto;
import com.example.spring.boot.hibernate.security.springboothibernatesecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto getUserRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto, BindingResult result) {
        User user = userService.findByEmail(userDto.getEmail());;
        if (user != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }

        if (result.hasErrors()) {
            return "registration";
        }


        userService.save(userDto);
        return "redirect:/registration?success";
    }
}
