package net.vesseldoc.server.model;

import java.io.Serializable;

/**
 * https://www.javainuse.com/spring/boot-jwt-mysql?fbclid=IwAR39k1k453l37iExXzKY7ugOJJpSZf8gs-cPhJaDG87F6GnpKeepHlLQCI0
 *
 * JWT Request object is a model to build a request for login purposes
 */
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String username;
    private String password;

    //need default constructor for JSON Parsing
    public JwtRequest() {
    }

    public JwtRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}