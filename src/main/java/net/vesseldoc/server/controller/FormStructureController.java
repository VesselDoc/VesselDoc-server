package net.vesseldoc.server.controller;

import net.vesseldoc.server.service.FormStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class FormStructureController {

    @Autowired
    private FormStructureService formStructureService;

    /**
     * Request to get a list of all form structures.
     *
     * @return List of form structures as json.
     */
    @GetMapping(value = "/structure/list")
    public List<List<Object>> getAllInfo() {
        return formStructureService.getAllInfo();
    }

    /**
     * Request to get form structure content.
     *
     * @param structureId form structure id.
     * @return Form structure content as bytearray.
     */
    @GetMapping(value = "/structure/get/{structureId:[0-9]+}")
    public byte[] getContent(@PathVariable long structureId) {
        return formStructureService.getContent(structureId);
    }

    /**
     * Request to upload new form structure.
     * Can only be done by a user with high authority.
     *
     * @param title form structure title.
     * @param file  form structure content as multipartfile.
     * @return Form structure id.
     * @throws IOException if data couldn't be stored.
     */
    @PostMapping(value = "/structure/set")
    public long uploadStructure(@RequestParam("title") String title, @RequestParam("content") MultipartFile file) throws IOException {
        return formStructureService.saveStructure(title, file);
    }

}
