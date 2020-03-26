package net.vesseldoc.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

@Service
public class FileService {

    @Autowired
    private FormService formService;

    private String dir = System.getProperty("user.home") + "/forms/";

    public byte[] getFile(String uuid) throws IOException {
        String date = new SimpleDateFormat("yyyyMMdd").format(formService.getForm(uuid).getCreationDate());
        File file = new File(dir + date + "/" + uuid);
        return Files.readAllBytes(file.toPath());
    }
}
