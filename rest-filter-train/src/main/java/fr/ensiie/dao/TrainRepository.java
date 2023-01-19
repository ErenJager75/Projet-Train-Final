package fr.ensiie.dao;

import fr.ensiie.entity.TrainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TrainRepository extends JpaRepository<TrainEntity, UUID> {

    List<TrainEntity> findAllByDepartureDateBetween(Instant beginDay, Instant endDay);

    List<TrainEntity> findAllByDepartureDateBetweenAndDepartureAndArrival(Instant beginDay, Instant endDay, String departure, String arrival);

    List<TrainEntity> findAllByDepartureAndArrival(String departure, String arrival);
}
