package net.vesseldoc.server.repository;

import net.vesseldoc.server.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * FormRepository is used to connect directly with the database about Form related data, if needed.
 */
@Repository
public interface FormRepository extends JpaRepository<Form, Long> {

    /**
     * Gets the last form created by the given user.
     *
     * @param userId User ID
     * @return Form ID of the last created form by given user.
     */
    @Query(value = "SELECT id FROM form f1 WHERE f1.user_id=:userId " +
            "AND f1.creation_date = (" +
            "SELECT MAX(creation_date) FROM form f2 WHERE f2.user_id = f1.user_id" +
            ")", nativeQuery = true)
    byte[] getLatestFormId(long userId);

    @Query(value = "SELECT * FROM form WHERE user_id=:userId", nativeQuery = true)
    List<Form> getAllByUserId(long userId);

    Form getById(UUID id);
}
