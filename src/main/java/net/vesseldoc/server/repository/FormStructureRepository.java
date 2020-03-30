package net.vesseldoc.server.repository;

import net.vesseldoc.server.model.FormStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormStructureRepository extends JpaRepository<FormStructure, Long> {

    @Query(value = "SELECT * FROM form_structure", nativeQuery = true)
    List<FormStructure> getAll();

    @Query(value = "SELECT content FROM form_structure WHERE id=:id", nativeQuery = true)
    byte[] getContentById(long id);
}
