package net.vesseldoc.server.controller;

import net.vesseldoc.server.service.FormStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

}
