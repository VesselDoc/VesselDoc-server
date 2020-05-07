package net.vesseldoc.server.repository;

import net.vesseldoc.server.model.DAOUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<DAOUser, Integer> {

    DAOUser findByUsername(String username);

    @Query(value = "SELECT * FROM user u WHERE username=:username AND active=1", nativeQuery = true)
    DAOUser getUserDetails(String username);

    @Query(value = "SELECT * FROM user WHERE active=1", nativeQuery = true)
    List<DAOUser> getUserList();

    DAOUser getDAOUserById(long userId);

}