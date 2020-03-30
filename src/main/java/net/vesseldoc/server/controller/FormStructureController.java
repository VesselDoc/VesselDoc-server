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

    @GetMapping(value = "/structure/list")
    public List<List<Object>> getAllInfo() {
        return formStructureService.getAllInfo();
    }

    @GetMapping(value = "/structure/get/{structureId:[0-9]+}")
    public byte[] getContent(@PathVariable long structureId) {
        return formStructureService.getContent(structureId);
    }

    @PostMapping(value = "/structure/set")
    public long uploadStructure(@RequestParam("title") String title, @RequestParam("content") MultipartFile file) throws IOException {
        return formStructureService.saveStructure(title, file);
    }

}
