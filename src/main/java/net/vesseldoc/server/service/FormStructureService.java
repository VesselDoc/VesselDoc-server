package net.vesseldoc.server.service;

import net.vesseldoc.server.model.Form;
import net.vesseldoc.server.model.FormStructure;
import net.vesseldoc.server.repository.FormStructureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class FormStructureService {

    @Autowired
    private FormStructureRepository formStructureRepository;

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

    public byte[] getContent(long id) {
        return formStructureRepository.getContentById(id);
    }
}
