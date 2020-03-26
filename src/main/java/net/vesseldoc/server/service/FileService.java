package net.vesseldoc.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    public void storeFile(MultipartFile file, String uuid) throws IOException {
        String date = new SimpleDateFormat("yyyyMMdd").format(formService.getForm(uuid).getCreationDate());
        Files.copy(file.getInputStream(), Paths.get(dir + date + "/" + uuid), StandardCopyOption.REPLACE_EXISTING);
    }
}
