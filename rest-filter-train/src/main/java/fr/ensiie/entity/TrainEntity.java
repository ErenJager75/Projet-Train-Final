package fr.ensiie.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "TRAIN")
public class TrainEntity {

    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "DEPARTURE")
    private String departure;

    @Column(name = "ARRIVAL")
    private String arrival;

    @Column(name = "IS_FLEXIBLE")
    private boolean isFlexible;

    @Column(name = "DEPARTURE_DATE")
    private Instant departureDate;

    @Column(name = "NUMBER_SEATS_STANDARD")
    private Integer numberSeatsStandard;

    @Column(name = "NUMBER_SEATS_BUSINESS")
    private Integer numberSeatsBusiness;

    @Column(name = "NUMBER_SEATS_FIRST")
    private Integer numberSeatsFirst;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRAIN_ID")
    private Set<ReservationEntity> reservationEntities;
}
