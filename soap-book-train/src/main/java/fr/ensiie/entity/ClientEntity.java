package fr.ensiie.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "CLIENT")
public class ClientEntity {

    @Id
    @Column(name = "MAIL")
    private String mail;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "PASSWORD")
    private String password;
}
