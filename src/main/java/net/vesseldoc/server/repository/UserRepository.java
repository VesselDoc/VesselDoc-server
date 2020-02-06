package net.vesseldoc.server.repository;

import net.vesseldoc.server.model.DAOUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<DAOUser, Integer> {
	
	DAOUser findByUsername(String username);
	/*
    @Query(value = "SELECT displayName FROM user u WHERE username=:username", nativeQuery = true)
    String findDisplayNameForUser(String username);
*/
    @Query(value = "SELECT * FROM user u WHERE username=:username", nativeQuery = true)
    DAOUser getUserDetails(String username);

}