package net.vesseldoc.server.controller;

import net.vesseldoc.server.model.DAOUser;
import net.vesseldoc.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @GetMapping(value = "/user/get/details")
    public DAOUser getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserDetails(auth.getName());
    }

    @GetMapping(value = "/user/get/list")
    public List<DAOUser> getUserList() {
        return userService.getAllUsers();
    }

    @PostMapping(value = "/user/set/password")
    public ResponseEntity changePassword(@RequestParam("current_password") String currentPassword,
                                         @RequestParam("new_password") String newPassword,
                                         @RequestParam(value = "username", required = false) String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (username == null || username.equals("")) {
            username = auth.getName();
        }

        return userService.changePassword(username, currentPassword, newPassword);
    }

    @PostMapping(value = "/user/set/role")
    public ResponseEntity setUserRole(@RequestParam("username") String username,
                                      @RequestParam("role") String role) {
        return userService.changeUserRole(username, role);
    }

    @PostMapping(value = "/user/set/deactivate")
    public ResponseEntity deactivateUser(@RequestParam("username") String username) {
        return userService.deactivateUser(username);
    }
}
