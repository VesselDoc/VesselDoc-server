package net.vesseldoc.server.service;

import net.vesseldoc.server.model.DAOUser;
import net.vesseldoc.server.repository.RoleRepository;
import net.vesseldoc.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }


    /**
     * Get User object from database.
     *
     * @return user object.
     */
    public DAOUser getUserDetails(String username) {
        return repository.getUserDetails(username);
    }

    public DAOUser getUserDetails(long id) {
        return repository.getDAOUserById(id);
    }

    /**
     * Gets the User ID for the user that is currently logged in.
     *
     * @return user ID for current user that is logged in.
     */
    public DAOUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return getUserDetails(auth.getName());
    }

    /**
     * Checks if the user who is currently logged in has a role that has high authority.
     *
     * @return true if user is high authority.
     */
    public boolean currentUserHasHighAuthority() {
        DAOUser user = getCurrentUser();

        if (getUserRole(user.getUsername()).equals("ADMIN")) {
            return true;
        } else if (getUserRole(user.getUsername()).equals("CAPTAIN")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Lists all users in the database.
     *
     * @return all users.
     */
    public List<DAOUser> getAllUsers() {
        return repository.getUserList();
    }

    /**
     * Changes password for specified user if old password matches.
     *
     * @param username username of the user that sre having it's password changed.
     * @param oldPassword existing password
     * @param newPassword new password.
     * @return response to tell if it was successfull or not.
     */
    public ResponseEntity<String> changePassword(String username , String oldPassword, String newPassword) {
        DAOUser user = repository.getUserDetails(username);

        if (!currentUserHasHighAuthority() || !getCurrentUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You dont have permission to change this users password!");
        } else if (!BCrypt.checkpw(oldPassword, user.getPassword()) && !currentUserHasHighAuthority()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password didn't match!");
        } else if (newPassword.equals(oldPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't change to the same password.");
        } else {
            user.setPassword(encoder.encode(newPassword));
            repository.save(user);
            return ResponseEntity.ok("Successfully changed password!");
        }

    }

    /**
     * Gets rolename of given user.
     *
     * @param username username.
     * @return name of role.
     */
    public String getUserRole(String username) {
        return getRoleName(repository.getUserDetails(username).getRoleId());
    }

    public String getRoleName(long roleId) {
        return roleRepository.getRoleById(roleId).getName();
    }

    /**
     * Changes role of given user.
     * Can only be changed by an user with high authority.
     * An user cannot change its own role.
     *
     * @param username username.
     * @param role new role.
     * @return Response to tell if the change was successful.
     */
    public ResponseEntity<String> changeUserRole(String username, String role) {
        ResponseEntity<String> response;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!currentUserHasHighAuthority()) {
            response = ResponseEntity.status(HttpStatus.FORBIDDEN).body("You need admin permission to do that!");
        } else if (auth.getName().equals(username)) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot change own role.");
        } else if (roleRepository.getRoleByName(role) == null) {
            response = ResponseEntity.status(HttpStatus.CONFLICT).body("Role dont exist!");
        } else if (repository.findByUsername(username) == null) {
            response = ResponseEntity.status(HttpStatus.CONFLICT).body("User dont exist!");
        } else if (repository.findByUsername(username).getRoleId() == roleRepository.getRoleByName(role).getId()) {
            response = ResponseEntity.status(HttpStatus.CONFLICT).body("User already have that role!");
        } else {
            DAOUser user = repository.findByUsername(username);
            user.setRoleId(roleRepository.getRoleByName(role).getId());
            repository.save(user);
            response = ResponseEntity.ok("Successfully changed users role!");
        }
        return response;
    }

    /**
     * Deactivates given user.
     * Can only be done by an user with high authority.
     *
     * @param username username.
     * @return Response to tell if the deactivation was successful.
     */
    public ResponseEntity<String> deactivateUser(String username) {
        if (!currentUserHasHighAuthority()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Current user is not allowed to do that.");
        } else if (getUserDetails(username) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Given user does not exist.");
        } else {
            DAOUser user = getUserDetails(username);
            user.setActive(false);
            repository.save(user);
            return ResponseEntity.ok("Successfully deactivated user.");
        }
    }
}
