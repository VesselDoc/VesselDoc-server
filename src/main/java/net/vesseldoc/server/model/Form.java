package net.vesseldoc.server.model;

import javax.persistence.*;

/**
 * This is the Form object.
 * If a new Form object is created, then hibernate will make sure it is placed in the connected databasse.
 */
@Entity
@Table(name = "form")
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long user_id;

    @Column(name = "form_structure_id")
    private long form_structure_id;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getForm_structure_id() {
        return form_structure_id;
    }

    public void setForm_structure_id(long form_structure_id) {
        this.form_structure_id = form_structure_id;
    }
}