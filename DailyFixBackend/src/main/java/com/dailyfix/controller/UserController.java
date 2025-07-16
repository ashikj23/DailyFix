package com.dailyfix.controller;

import com.dailyfix.model.User;
import com.dailyfix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Fetch logged-in user profile
    @GetMapping("/profile")
    public User getProfile(Principal principal) {
        return userService.findOrCreateUser(principal);
    }
}
