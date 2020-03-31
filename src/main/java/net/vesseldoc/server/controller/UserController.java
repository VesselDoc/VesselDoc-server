package net.vesseldoc.server.controller;

import net.vesseldoc.server.model.DAOUser;
import net.vesseldoc.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /*
        @GetMapping(value = "/getDisplayName")
        public String getDisplayName() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return userService.getDisplayNameForUser(auth.getName());
        }
    */
    @GetMapping(value = "/getUserDetails")
    public DAOUser getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserDetails(auth.getName());
    }

}


