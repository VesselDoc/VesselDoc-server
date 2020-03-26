package net.vesseldoc.server.controller;

import net.vesseldoc.server.service.FormService;
import net.vesseldoc.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        long userId = userService.getCurrentUser();
        return formService.save(userId, structureId).toString();
    }

    @GetMapping(value = "/getUsersForms")
    public List<List<Object>> getCurrentUsersForms() {
        long userId = userService.getCurrentUser();
        return formService.getAllFormsByUser(userId);
    }

}
