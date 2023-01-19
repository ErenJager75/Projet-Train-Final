package fr.ensiie.repository.db;

import fr.ensiie.entity.ClientEntity;
import fr.ensiie.entity.ClientReservationEntity;
import fr.ensiie.model.xml.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<ClientReservationEntity, UUID> {
    List<ClientReservationEntity> findAllByClientEntity_Mail(String mail);
}
