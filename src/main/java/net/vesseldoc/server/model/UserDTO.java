package net.vesseldoc.server.model;

/**
 * https://www.javainuse.com/spring/boot-jwt-mysql?fbclid=IwAR39k1k453l37iExXzKY7ugOJJpSZf8gs-cPhJaDG87F6GnpKeepHlLQCI0
 *
 * UserDTO is responsible for serving user information to JwtControllers and services.
 */
public class UserDTO {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}