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

    /*
    /**
     * Get displayName for user
     *
     * @return the display name
     *
    public String getDisplayNameForUser(String username) { return repository.findDisplayNameForUser(username);
    }
*/

    /**
     * Get UserDetails for user
     *
     * @return the user details
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

    public List<DAOUser> getAllUsers() {
        return repository.getUserList();
    }

    public ResponseEntity<String> changePassword(String username , String oldPassword, String newPassword) {
        DAOUser user = repository.getUserDetails(username);

        if (BCrypt.checkpw(oldPassword, user.getPassword())) {
            if (!newPassword.equals(oldPassword)) {
                user.setPassword(encoder.encode(newPassword));
                repository.save(user);
                return ResponseEntity.ok("Successfully changed password!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can't change to the same password.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password didn't match!");
        }

    }

    public String getUserRole(String username) {
        return getRoleName(repository.getUserDetails(username).getRoleId());
    }

    public String getRoleName(long roleId) {
        return roleRepository.getRoleById(roleId).getName();
    }

    public ResponseEntity changeUserRole(String username, String role) {
        ResponseEntity response;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!getUserRole(auth.getName()).equals("ADMIN")) {
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
            // Do job!
            DAOUser user = repository.findByUsername(username);
            user.setRoleId(roleRepository.getRoleByName(role).getId());
            repository.save(user);
            response = ResponseEntity.ok("Successfully changed users role!");
        }
        return response;
    }
}
