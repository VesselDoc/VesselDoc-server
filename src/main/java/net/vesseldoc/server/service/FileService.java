package net.vesseldoc.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    private String dir = "/var/vesseldoc/forms/";

    public byte[] getForm(String uuid) throws IOException {
        String date = new SimpleDateFormat("yyyyMMdd").format(formService.getForm(uuid).getCreationDate());
        File file = new File(dir + date + "/" + uuid);
        return Files.readAllBytes(file.toPath());
    }

    public ResponseEntity storeForm(MultipartFile file, String uuid) throws IOException {
        String date = new SimpleDateFormat("yyyyMMdd").format(formService.getForm(uuid).getCreationDate());
        File path = new File(dir + date);
        if (! path.exists()) {
            path.mkdirs();
        }
        Files.copy(file.getInputStream(), Paths.get(dir + date + "/" + uuid), StandardCopyOption.REPLACE_EXISTING);
        return ResponseEntity.ok("Form successfully saved!");
    }

    public boolean formExists(String uuid) {
        String date = new SimpleDateFormat("yyyyMMdd").format(formService.getForm(uuid).getCreationDate());
        File path = new File(dir + date);
        return path.exists();
    }
}
