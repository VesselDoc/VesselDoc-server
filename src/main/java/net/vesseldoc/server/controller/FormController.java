package net.vesseldoc.server.controller;

import net.vesseldoc.server.service.FormService;
import net.vesseldoc.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class FormController {

    @Autowired
    private UserService userService;

    @Autowired
    private FormService formService;

    /**
     * Creates a new empty form which is attached to a user and a Form structure.
     * @param structureId ID of the form structure this form is based on.
     * @return Form ID.
     */
    @PostMapping(value = "/newForm")
    public String newForm(@RequestParam("structure_id") long structureId) {

        // These lines gets the User ID for the user that is currently logged in.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long userId = userService.getUserDetails(auth.getName()).getId();

        return formService.save(userId, structureId).toString();
    }

}
