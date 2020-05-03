package net.vesseldoc.server.controller;

import net.vesseldoc.server.model.DAOUser;
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

    /**
     * Request to get a list of forms including info abouth them.
     *
     * @return Form list as json.
     */
    @GetMapping(value = "/form/list")
    public ResponseEntity<List<List<Object>>> getFormList() {
        DAOUser user = userService.getCurrentUser();
        if (userService.currentUserHasHighAuthority()) {
            return formService.getAllForms();
        } else {
            return formService.getAllFormsByUser(user.getId());
        }
    }

    /**
     * Request to list all forms that is not signed.
     *
     * @return Form list as json.
     */
    @GetMapping(value = "/form/list/notsigned")
    public ResponseEntity<List<List<Object>>> getCurrentUsersForms() {
        DAOUser user = userService.getCurrentUser();
        if (userService.currentUserHasHighAuthority()) {
            return formService.getAllUnsigned();
        } else {
            return formService.getAllUnsignedByUser(user.getId());
        }
    }

    /**
     * Request to get form content.
     *
     * @param formId form id.
     * @return Form content as bytearray.
     * @throws IOException if the file dont exist.
     */
    @GetMapping(value = "/form/get/{formId:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getFormFile(@PathVariable String formId) throws IOException {
        ByteArrayResource file = new ByteArrayResource(fileService.getForm(formId));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; formId=\"" + formId + "\"")
                .body(file);
    }

    /**
     * Request to upload form to a specified form id.
     *
     * @param file   form content as bytearray.
     * @param formId form id.
     * @return Response to tell if the upload was successful.
     * @throws IOException if the file cant be stored.
     */
    @PostMapping(value = "/form/set")
    public ResponseEntity<String> uploadFormFile(@RequestParam("file") MultipartFile file, @RequestParam("id") String formId) throws IOException {
        return fileService.storeForm(file, formId);
    }

    /**
     * Request for signing a form.
     * User needs high authority to do this.
     *
     * @param formId form id.
     * @return Response to tell if the signing was successful.
     */
    @PostMapping(value = "/form/set/sign")
    public ResponseEntity<String> signForm(@RequestParam("form_id") String formId) {
        return formService.signForm(formId);
    }

    /**
     * Request to check signed status of a specified form id.
     *
     * @param formId form id.
     * @return Response to with status. True if it is signed, false if not.
     */
    @GetMapping(value = "/form/get/signed/{formId:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    public ResponseEntity<Boolean> getSignedStatus(@PathVariable String formId) {
        return ResponseEntity.ok(formService.isSigned(formId));
    }
}
