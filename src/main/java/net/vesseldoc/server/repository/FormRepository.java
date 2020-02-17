package net.vesseldoc.server.repository;

import net.vesseldoc.server.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * FormRepository is used to connect directly with the database about Form related data, if needed.
 */
@Repository
public interface FormRepository extends JpaRepository<Form, Long> {

    /**
     * Gets the last form created by the given user.
     * @param userId User ID
     * @return Form ID of the last created form by given user.
     */
    @Query(value = "SELECT MAX(id) FROM form WHERE user_id=:userId", nativeQuery = true)
    long getFormId(long userId);

}
