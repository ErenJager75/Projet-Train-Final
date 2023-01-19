package fr.ensiie.model;

import fr.ensiie.entity.TrainEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "Ressource représentant un Train")
@NoArgsConstructor
public class Train {

    @Schema(description = "L'id du train")
    private UUID id;
    @Schema(description = "La gare de départ du train")
    private String departure;
    @Schema(description = "La gare d'arrivée du train")
    private String arrival;
    @Schema(description = "Les réservations avec ce train sont-elles flexibles")
    private Boolean isFlexible;
    @Schema(description = "La date de départ")
    private Instant departureDate;
    @Schema(description = "Le nombre de places en classe STANDARD restantes")
    private Integer numberSeatsStandard;
    @Schema(description = "Le nombre de places en classe BUSINESS restantes")
    private Integer numberSeatsBusiness;
    @Schema(description = "Le nombre de places en classe FIRST restantes")
    private Integer numberSeatsFirst;

    public Train(TrainEntity trainEntity) {
        this.id = trainEntity.getId();
        this.departure = trainEntity.getDeparture();
        this.arrival = trainEntity.getArrival();
        this.isFlexible = trainEntity.isFlexible();
        this.departureDate = trainEntity.getDepartureDate();
        int numberSeatsStandardBooked = 0;
        int numberSeatsBusinessBooked = 0;
        int numberSeatsFirstBooked = 0;
        if (trainEntity.getReservationEntities() != null) {
            numberSeatsStandardBooked = (int) trainEntity.getReservationEntities()
                    .stream()
                    .filter(reservationEntity -> reservationEntity.getClasseEntity()
                            .getName()
                            .equals("STANDARD"))
                    .count();

            numberSeatsBusinessBooked = (int) trainEntity.getReservationEntities()
                    .stream()
                    .filter(reservationEntity -> reservationEntity.getClasseEntity()
                            .getName()
                            .equals("BUSINESS"))
                    .count();

            numberSeatsFirstBooked = (int) trainEntity.getReservationEntities()
                    .stream()
                    .filter(reservationEntity -> reservationEntity.getClasseEntity()
                            .getName()
                            .equals("FIRST"))
                    .count();
        }
        this.numberSeatsStandard = trainEntity.getNumberSeatsStandard() - numberSeatsStandardBooked;
        this.numberSeatsBusiness = trainEntity.getNumberSeatsBusiness() - numberSeatsBusinessBooked;
        this.numberSeatsFirst = trainEntity.getNumberSeatsFirst() - numberSeatsFirstBooked;
    }

    public TrainEntity toEntity(boolean generateId) {
        TrainEntity trainEntity = new TrainEntity();
        if (generateId) {
            trainEntity.setId(UUID.randomUUID());
            trainEntity.setDeparture(this.departure.toUpperCase())
                    .setArrival(this.arrival.toUpperCase());
        }
        return trainEntity
                .setFlexible(this.isFlexible)
                .setDepartureDate(this.departureDate)
                .setNumberSeatsBusiness(this.numberSeatsBusiness)
                .setNumberSeatsFirst(this.numberSeatsFirst)
                .setNumberSeatsStandard(this.numberSeatsStandard);
    }
}
