package net.vesseldoc.server.controller;

import net.vesseldoc.server.service.FileService;
import net.vesseldoc.server.service.FormService;
import net.vesseldoc.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class FormController {

    @Autowired
    private UserService userService;

    @Autowired
    private FormService formService;

    @Autowired
    private FileService fileService;

    /**
     * Creates a new empty form which is attached to a user and a Form structure.
     *
     * @param structureId ID of the form structure this form is based on.
     * @return Form ID.
     */
    @PostMapping(value = "/newForm")
    public String newForm(@RequestParam("structure_id") long structureId) {
        long userId = userService.getCurrentUser().getId();
        return formService.save(userId, structureId).toString();
    }

    @GetMapping(value = "/form/list")
    public List<List<Object>> getCurrentUsersForms() {
        long userId = userService.getCurrentUser().getId();
        return formService.getAllFormsByUser(userId);
    }

    @GetMapping(value = "/form/get/{formId:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getFormFile(@PathVariable String formId) throws IOException {
        ByteArrayResource file = new ByteArrayResource(fileService.getForm(formId));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; formId=\"" + formId + "\"")
                .body(file);
    }

    @PostMapping(value = "/form/set")
    public ResponseEntity<String> uploadFormFile(@RequestParam("file") MultipartFile file, @RequestParam("id") String formId) throws IOException {
        return fileService.storeForm(file, formId);
    }

    @PostMapping(value = "/form/set/sign")
    public ResponseEntity<String> signForm(@RequestParam("form_id") String formId) {
        return formService.signForm(formId);
    }

    @GetMapping(value = "/form/get/signed/{formId:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    public ResponseEntity<Boolean> getSignedStatus(@PathVariable String formId) {
        return ResponseEntity.ok(formService.isSigned(formId));
    }
}
