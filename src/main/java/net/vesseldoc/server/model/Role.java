package net.vesseldoc.server.model;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role {

    @Id
    private long id;
    @Column
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
