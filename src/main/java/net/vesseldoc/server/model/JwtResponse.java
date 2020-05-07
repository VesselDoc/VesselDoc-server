package net.vesseldoc.server.model;

import java.io.Serializable;

/**
 * https://www.javainuse.com/spring/boot-jwt-mysql?fbclid=IwAR39k1k453l37iExXzKY7ugOJJpSZf8gs-cPhJaDG87F6GnpKeepHlLQCI0
 *
 * Model that can be sent as a response.
 */
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;

    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getToken() {
        return this.jwttoken;
    }
}