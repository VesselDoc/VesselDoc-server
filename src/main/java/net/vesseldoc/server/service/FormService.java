package net.vesseldoc.server.service;

import net.vesseldoc.server.model.Form;
import net.vesseldoc.server.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.UUID;

@Service
public class FormService {

    @Autowired
    private FormRepository formRepository;

    /**
     * This creates a new form object, sends the information to the database and returns and ID.
     * @param userId User ID
     * @param structureId Form structure ID
     * @return Form ID of the newly created form
     */
    public UUID save(long userId, long structureId){
        Form form = new Form();
        form.setUser_id(userId);
        form.setForm_structure_id(structureId);

        formRepository.save(form);
        return getLatestFormByUser(userId);
    }

    /**
     * Gets the latest created Form ID by the given user.
     * @param userId User ID
     * @return Form ID
     */
    public UUID getLatestFormByUser(long userId) {
        byte[] uuidAsBytes = formRepository.getLatestFormId(userId);
        ByteBuffer b = ByteBuffer.wrap(uuidAsBytes);
        UUID uuid = new UUID(b.getLong(), b.getLong());
        return uuid;
    }
}
