package net.vesseldoc.server.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * This is the Form object.
 * If a new Form object is created, then hibernate will make sure it is placed in the connected databasse.
 */
@Entity
@Table(name = "form")
public class Form {

    /**
     * https://thoughts-on-java.org/generate-uuids-primary-keys-hibernate/
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_id")
    private long user_id;

    @Column(name = "form_structure_id")
    private long form_structure_id;

    @CreationTimestamp
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

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
