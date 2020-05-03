package net.vesseldoc.server.controller;

import net.vesseldoc.server.config.JwtTokenUtil;
import net.vesseldoc.server.model.JwtRequest;
import net.vesseldoc.server.model.JwtResponse;
import net.vesseldoc.server.model.UserDTO;
import net.vesseldoc.server.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    /**
     * Request to log in and return a bearer token.
     *
     * @param authenticationRequest JwtRequest, which requires a username and a password.
     * @return Response with bearer token.
     * @throws Exception if username and password dont match.
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    /**
     * Request to register new user.
     *
     * @param user UserDTO, which requires a username and a password.
     * @return Response with user information if it was successful.
     * @throws Exception if user already exist or if username or password is empty.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
        if (user.getUsername() == "" || user.getPassword() == "") {
            return new ResponseEntity<String>("username/password cannot be empty.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(userDetailsService.save(user));
    }

    /**
     * Authenticates with an authentication manager that have a set of checks.
     * If one of these checks is triggered, then it returns a exception instead.
     *
     * @param username username.
     * @param password password.
     * @throws Exception if one of the checks is triggered.
     */
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}