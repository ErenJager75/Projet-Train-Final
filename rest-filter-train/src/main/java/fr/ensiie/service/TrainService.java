package fr.ensiie.service;

import fr.ensiie.dao.TrainRepository;
import fr.ensiie.entity.TrainEntity;
import fr.ensiie.model.Train;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainService {

    private final TrainRepository trainRepository;

    public List<Train> list() {
        return this.trainRepository.findAll()
                .stream()
                .map(Train::new)
                .filter(train -> train.getNumberSeatsBusiness() > 0
                        || train.getNumberSeatsStandard() > 0
                        || train.getNumberSeatsFirst() > 0)
                .collect(Collectors.toList());
    }

    public Train read(UUID id) {
        Optional<TrainEntity> trainEntity = trainRepository.findById(id);
        if (trainEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Le Train avec l'id passé en paramètre n'existe pas");
        }
        return new Train(trainEntity.get());
    }

    public List<Train> listByDepartureAndArrival(String departure, String arrival) {
        return this.trainRepository.findAllByDepartureAndArrival(departure, arrival)
                .stream()
                .map(Train::new)
                .filter(train -> train.getNumberSeatsBusiness() > 0
                        || train.getNumberSeatsStandard() > 0
                        || train.getNumberSeatsFirst() > 0)
                .collect(Collectors.toList());
    }

    public List<Train> listByDateAndDepartureAndArrival(Instant date, String departure, String arrival) {
        Instant[] bounds = getBoundsFromDate(date);

        return this.trainRepository.findAllByDepartureDateBetweenAndDepartureAndArrival(bounds[0], bounds[1], departure, arrival)
                .stream()
                .map(Train::new)
                .filter(train -> train.getNumberSeatsBusiness() > 0
                        || train.getNumberSeatsStandard() > 0
                        || train.getNumberSeatsFirst() > 0)
                .collect(Collectors.toList());
    }

    public List<Train> listByDateAndDepartureAndArrivalAndClasse(Instant date, String departure, String arrival, String classe) {
        Instant[] bounds = getBoundsFromDate(date);

        return this.trainRepository.findAllByDepartureDateBetweenAndDepartureAndArrival(bounds[0], bounds[1], departure, arrival)
                .stream()
                .map(Train::new)
                .filter(train -> {
                    if (classe.equalsIgnoreCase("STANDARD")) {
                        return train.getNumberSeatsStandard() > 0;
                    } else if (classe.equalsIgnoreCase("BUSINESS")) {
                        return train.getNumberSeatsBusiness() > 0;
                    } else if (classe.equalsIgnoreCase("FIRST")) {
                        return train.getNumberSeatsFirst() > 0;
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    public Train create(Train train) {
        return new Train(trainRepository.save(train.toEntity(true)));
    }

    private Instant[] getBoundsFromDate(Instant date) {
        Instant[] bounds = new Instant[2];


        LocalTime startOfDay = LocalTime.MIN;
        LocalTime endOfDay = LocalTime.MAX;
        ZoneId zoneId = ZoneId.of("Europe/Paris"); // fuseau horaire de Paris
        ZonedDateTime dateZoned = date.atZone(zoneId);

        bounds[0] = ZonedDateTime.of(dateZoned.toLocalDate(), startOfDay, zoneId).toInstant();
        bounds[1] = ZonedDateTime.of(dateZoned.toLocalDate(), endOfDay, zoneId).toInstant();
        return bounds;
    }



}
