package fr.ensiie.service;

import fr.ensiie.dao.ReservationRepository;
import fr.ensiie.dao.TrainRepository;
import fr.ensiie.entity.ReservationEntity;
import fr.ensiie.entity.TrainEntity;
import fr.ensiie.model.Reservation;
import fr.ensiie.model.Train;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final TrainRepository trainRepository;

    public Reservation read(UUID id) {
        Optional<ReservationEntity> reservationEntity = reservationRepository.findById(id);
        if (reservationEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La réservation avec l'id passé en paramètre n'existe pas");
        }
        return new Reservation(reservationEntity.get());
    }

    public Reservation create(Reservation reservation) {
        Optional<TrainEntity> trainEntity = trainRepository.findById(reservation.getTrain().getId());
        if (trainEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Le Train pour lequel vous faites la réservation n'existe pas");
        }
        Train train = new Train(trainEntity.get());
        if (reservation.getClasse().equalsIgnoreCase("STANDARD") && train.getNumberSeatsStandard() <= 0) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Il n'y a plus de places en classe STANDARD");
        }
        if (reservation.getClasse().equalsIgnoreCase("BUSINESS") && train.getNumberSeatsBusiness() <= 0) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Il n'y a plus de places en classe BUSINESS");
        }
        if (reservation.getClasse().equalsIgnoreCase("FIRST") && train.getNumberSeatsFirst() <= 0) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Il n'y a plus de places en classe FIRST");
        }
        reservation.setTrain(train);
        Reservation reservationSaved = new Reservation(reservationRepository.saveAndFlush(reservation.toEntity(true).setTrainEntity(trainEntity.get())));
        return reservationSaved.setTrain(new Train(trainRepository.findById(reservationSaved.getTrain().getId()).orElseThrow()));
    }


    public void delete(UUID id) {
        Optional<ReservationEntity> reservationEntity = reservationRepository.findById(id);
        if (reservationEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La réservation avec l'id passé en paramètre n'existe pas");
        }
        try {
            reservationRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "La réservation n'a pas pu être supprimée");
        };
    }
}
