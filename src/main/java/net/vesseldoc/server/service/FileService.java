package net.vesseldoc.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /**
     * Gets form file by a specified form id.
     *
     * @param uuid form id.
     * @return file as bytearray.
     * @throws IOException if file isn't found.
     */
    public byte[] getForm(String uuid) throws IOException {
        String date = new SimpleDateFormat("yyyyMMdd").format(formService.getForm(uuid).getCreationDate());
        File file = new File(dir + date + "/" + uuid);
        return Files.readAllBytes(file.toPath());
    }

    /**
     * Store a form file.
     *
     * @param file file as multipartfile.
     * @param uuid form id.
     * @return Response to give feedback if the file was successfully stored or not.
     * @throws IOException if file couldn't be stored.
     */
    public ResponseEntity<String> storeForm(MultipartFile file, String uuid) throws IOException {
        ResponseEntity<String> response;
        if (formService.isSigned(uuid)) {
            response = ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot update form when its already signed.");
        } else {
            String date = new SimpleDateFormat("yyyyMMdd").format(formService.getForm(uuid).getCreationDate());
            File path = new File(dir + date);
            if (!path.exists()) {
                path.mkdirs();
            }
            Files.copy(file.getInputStream(), Paths.get(dir + date + "/" + uuid), StandardCopyOption.REPLACE_EXISTING);
            response = ResponseEntity.ok("Form successfully saved!");
        }

        return response;
    }

    /**
     * Checks if there exist a file with to the given form id.
     *
     * @param uuid form id.
     * @return true if it exists, false if not.
     */
    public boolean formExists(String uuid) {
        String date = new SimpleDateFormat("yyyyMMdd").format(formService.getForm(uuid).getCreationDate());
        File path = new File(dir + date);
        return path.exists();
    }
}
