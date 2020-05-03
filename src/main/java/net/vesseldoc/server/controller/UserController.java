package net.vesseldoc.server.controller;

import net.vesseldoc.server.model.DAOUser;
import net.vesseldoc.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Request to get details of the user that is currently logged in.
     *
     * @return user details as json.
     */
    @GetMapping(value = "/user/get/details")
    public DAOUser getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserDetails(auth.getName());
    }

    /**
     * Request to get list of users including information about them.
     *
     * @return user list as json.
     */
    @GetMapping(value = "/user/get/list")
    public List<DAOUser> getUserList() {
        return userService.getAllUsers();
    }

    /**
     * Request to change password.
     * If username is specified, then it is required to have a high authority role.
     * If no username is specified then the possword to the current logged in user is going to be changed.
     *
     * @param currentPassword current password.
     * @param newPassword new password.
     * @param username username.
     * @return Response to tell if the password change was successful.
     */
    @PostMapping(value = "/user/set/password")
    public ResponseEntity<String> changePassword(@RequestParam("current_password") String currentPassword,
                                         @RequestParam("new_password") String newPassword,
                                         @RequestParam(value = "username", required = false) String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (username == null || username.equals("")) {
            username = auth.getName();
        }

        return userService.changePassword(username, currentPassword, newPassword);
    }

    /**
     * Request to change role of a specified user.
     * Can only be done by a user with high authority.
     *
     * @param username username.
     * @param role role as string eg. 'ADMIN' or 'WORKER'.
     * @return Response to tell iw the change was successful.
     */
    @PostMapping(value = "/user/set/role")
    public ResponseEntity<String> setUserRole(@RequestParam("username") String username,
                                      @RequestParam("role") String role) {
        return userService.changeUserRole(username, role);
    }

    /**
     * Request to deactivate user.
     *
     * @param username username.
     * @return Response to tell if the deactivation was successful.
     */
    @PostMapping(value = "/user/set/deactivate")
    public ResponseEntity<String> deactivateUser(@RequestParam("username") String username) {
        return userService.deactivateUser(username);
    }
}
