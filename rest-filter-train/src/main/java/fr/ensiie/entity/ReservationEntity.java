package fr.ensiie.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "RESERVATION_TRAIN")
public class ReservationEntity {

    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLASS_ID")
    private ClasseEntity classeEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TRAIN_ID")
    private TrainEntity trainEntity;
}
