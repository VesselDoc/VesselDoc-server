package net.vesseldoc.server.service;

import net.vesseldoc.server.model.DAOUser;
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

    private UserRepository repository;

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

    /**
     * Gets the User ID for the user that is currently logged in.
     *
     * @return user ID for current user that is logged in.
     */
    public long getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return getUserDetails(auth.getName()).getId();
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

}
