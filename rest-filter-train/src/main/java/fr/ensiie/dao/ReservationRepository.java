package fr.ensiie.dao;

import fr.ensiie.entity.ReservationEntity;
import fr.ensiie.entity.TrainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID> {

}
