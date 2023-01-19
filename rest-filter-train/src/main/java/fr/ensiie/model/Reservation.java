package fr.ensiie.model;

import fr.ensiie.entity.ClasseEntity;
import fr.ensiie.entity.ReservationEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "Ressource représentant une Réservation sur un Train")
@NoArgsConstructor
@Accessors(chain = true)
public class Reservation {

    @Schema(description = "L'id de la réservation")
    private UUID id;
    @Schema(description = "Le nom de la personne qui a réservé")
    private String lastName;
    @Schema(description = "Le prénom de la personne qui a réservé")
    private String firstName;
    @Schema(description = "La classe de la réservation, elle peut être \"STANDARD\", \"FIRST\" ou \"BUSINESS\"")
    private String classe;
    @Schema(description = "Le train auquel la réservation est associée")
    private Train train;

    public Reservation(ReservationEntity reservationEntity) {
        this.id = reservationEntity.getId();
        this.lastName = reservationEntity.getLastName();
        this.firstName = reservationEntity.getFirstName();
        this.classe = reservationEntity.getClasseEntity().getName();
        this.train = new Train(reservationEntity.getTrainEntity());
    }

    public ReservationEntity toEntity(boolean generateId) {
        ReservationEntity reservationEntity = new ReservationEntity();
        if (generateId) {
            reservationEntity.setId(UUID.randomUUID());
        }
        return reservationEntity.setClasseEntity(new ClasseEntity()
                        .setId(ClasseEnum.getByName(this.classe)
                                .getId())
                        .setName(this.classe.toUpperCase()))
                .setTrainEntity(this.train.toEntity(false))
                .setLastName(this.lastName)
                .setFirstName(this.firstName);

    }
}
