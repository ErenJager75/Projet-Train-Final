package fr.ensiie.model.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class TrainREST {

    private UUID id;
    private String departure;
    private String arrival;
    private Boolean isFlexible;
    private Instant departureDate;
    private Integer numberSeatsStandard;
    private Integer numberSeatsBusiness;
    private Integer numberSeatsFirst;

}
