package net.vesseldoc.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * DAOUser is the user object. It is attached to the user table in the database and can be used for authentication.
 */
@Entity
@Table(name = "user")
public class DAOUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String username;

    @Column
    @JsonIgnore
    private String password;

    @Column(name = "role_id")
    private long roleId;

    /**
     * This tells if the user is deleted or not.
     * This is done instead of actually deleting a user
     * because it wont mess with other database objects that has user as foreign key.
     */
    private boolean active;

    public long getId() {
        return id;
    }

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

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}