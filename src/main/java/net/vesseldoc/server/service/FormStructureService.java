package net.vesseldoc.server.service;

import net.vesseldoc.server.model.FormStructure;
import net.vesseldoc.server.repository.FormStructureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class FormStructureService {

    @Autowired
    private FormStructureRepository formStructureRepository;

    /**
     * Gets a list of all form structures.
     *
     * @return list of form structures.
     */
    public List<List<Object>> getAllInfo() {
        List<FormStructure> dbContent = formStructureRepository.getAll();
        List<List<Object>> list = new ArrayList<List<Object>>();
        Iterator<FormStructure> it = dbContent.iterator();
        while (it.hasNext()) {
            FormStructure fs = it.next();
            list.add(Arrays.asList(fs.getId(), fs.getTitle()));
        }
        return list;
    }

    /**
     * Gets content of a given form structure id.
     *
     * @param id form structure id:
     * @return form structure content as bytearray.
     */
    public byte[] getContent(long id) {
        return formStructureRepository.getContentById(id);
    }

    /**
     * Saves a form structure and returns its id.
     *
     * @param title form structure title.
     * @param file form structure content as multipartfile.
     * @return form structure id.
     * @throws IOException if file cant be saved.
     */
    public long saveStructure(String title, MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        FormStructure structure = new FormStructure();
        structure.setTitle(title);
        structure.setContent(bytes);

        return formStructureRepository.save(structure).getId();
    }

    /**
     * Gets form structure with info from database.
     *
     * @param id form structure id.
     * @return form structure object.
     */
    public FormStructure getFormStructure(long id) {
        return formStructureRepository.getOne(id);
    }
}
