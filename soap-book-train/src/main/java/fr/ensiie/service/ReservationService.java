package fr.ensiie.service;

import fr.ensiie.config.CompanyConfig;
import fr.ensiie.entity.ClientEntity;
import fr.ensiie.entity.ClientReservationEntity;
import fr.ensiie.entity.CompanyEntity;
import fr.ensiie.exception.*;
import fr.ensiie.model.rest.ReservationREST;
import fr.ensiie.model.rest.TrainREST;
import fr.ensiie.model.xml.*;
import fr.ensiie.repository.db.ReservationRepository;
import fr.ensiie.repository.rest.ReservationRESTRepository;
import fr.ensiie.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReservationService {

    private final ReservationRESTRepository reservationRESTRepository;

    private final ReservationRepository reservationRepository;

    private final List<CompanyConfig> companyConfigurations;

    private final JWTUtil jwtUtil;

    public MakeReservationsResponse create(MakeReservationsRequest request) {
        Claims claimsJwt = jwtUtil.getTokenInformation(request.getJWTtoken());
        String mail = claimsJwt.get("mail", String.class);
        String lastName = claimsJwt.get("lastName", String.class);
        String firstName = claimsJwt.get("firstName", String.class);
        List<ReservationREST> reservationsAchieved = new ArrayList<>();
        List<CompanyConfig> companyReservationsAchieved = new ArrayList<>();
        boolean doWeNeedARollback = false;
        for (MakeReservationsRequestSingle reservationRequestSingle : request.getReservationSingle()) {
            for (int i = 0; i < reservationRequestSingle.getNumberPlaces(); i++) {
                ReservationREST reservationREST = new ReservationREST()
                        .setId(UUID.randomUUID())
                        .setFirstName(firstName)
                        .setLastName(lastName)
                        .setClasse(reservationRequestSingle.getClasse().value())
                        .setTrain(new TrainREST().setId(UUID.fromString(reservationRequestSingle.getIdTrain())));
                CompanyConfig companyConfig = null;
                for (CompanyConfig companyConfiguration : companyConfigurations) {
                    if (companyConfiguration.getName().equals(reservationRequestSingle.getCompany().value())) {
                        companyConfig = companyConfiguration;
                    }
                }
                try {
                    if (companyConfig == null) {
                        log.info("Pas de config");
                        throw new Exception();
                    }
                    reservationsAchieved.add(this.reservationRESTRepository.postReservationREST(reservationREST, companyConfig.getUrl()));
                    companyReservationsAchieved.add(companyConfig);
                } catch (Exception e) {
                    log.info(e.getMessage());
                    doWeNeedARollback = true;
                }
            }
        }
        if (doWeNeedARollback) {
            rollback(reservationsAchieved, companyReservationsAchieved);
            throw new ConflictException("Une des réservations n'était plus possible");
        }
        this.saveReservationsToDatabase(reservationsAchieved, companyReservationsAchieved, mail);
        MakeReservationsResponse reservationsResponseComplete = new MakeReservationsResponse();
        reservationsResponseComplete.setMessage("Succès de l'ajout");
        return reservationsResponseComplete;
    }

    public GetReservationsResponse findAll(GetReservationsRequest request) {
        Claims claimsJwt = jwtUtil.getTokenInformation(request.getJWTtoken());
        String mail = claimsJwt.get("mail", String.class);
        List<ClientReservationEntity> clientReservationEntities = reservationRepository.findAllByClientEntity_Mail(mail);
        GetReservationsResponse response = new GetReservationsResponse();
        List<Reservation> reservations = response.getReservation();
        clientReservationEntities.forEach(clientReservation -> {
            CompanyConfig companyConfigFind = companyConfigurations
                    .stream()
                    .filter(companyConfig -> companyConfig.getId() == clientReservation.getCompanyEntity().getId())
                    .findFirst().get();
            ReservationREST reservationREST = reservationRESTRepository.getReservationREST(clientReservation.getReservationId(), companyConfigFind.getUrl());
            try {
                reservations.add(reservationREST.toReservationXML(clientReservation.getId() ,companyConfigFind.getName()));
            } catch (DatatypeConfigurationException e) {
                throw new RuntimeException(e);
            }
        });
        reservations.sort((o1, o2) -> o1.getTrain().getDepartureDate().compare(o2.getTrain().getDepartureDate()));
        return response;
    }

    private void saveReservationsToDatabase(List<ReservationREST> reservationsAchieved,
                                            List<CompanyConfig> companyReservationsAchieved,
                                            String mail) {
        List<ClientReservationEntity> clientReservationEntities = new ArrayList<>();
        for (int i = 0; i < reservationsAchieved.size(); i++) {
            ClientReservationEntity clientReservation = new ClientReservationEntity();
            clientReservation.setId(UUID.randomUUID())
                    .setClientEntity(new ClientEntity().setMail(mail))
                    .setReservationId(reservationsAchieved.get(i).getId())
                    .setCompanyEntity(new CompanyEntity().setId(companyReservationsAchieved.get(i).getId()));
            clientReservationEntities.add(clientReservation);

        }
        reservationRepository.saveAll(clientReservationEntities);
    }


    public void rollback(List<ReservationREST> reservationREST, List<CompanyConfig> companyReservationsAchieved) {
        for (int i = 0; i < reservationREST.size(); i++) {
            reservationRESTRepository.deleteReservation(reservationREST.get(i).getId().toString(), companyReservationsAchieved.get(i).getUrl());
        }
    }


    public DeleteReservationResponse delete(DeleteReservationRequest request) {
        Claims claimsJwt = jwtUtil.getTokenInformation(request.getJWTtoken());
        String mail = claimsJwt.get("mail", String.class);
        Optional<ClientReservationEntity> reservationEntityOptional;
        try {
            reservationEntityOptional = reservationRepository.findById(UUID.fromString(request.getIdReservation()));
        } catch (Exception e) {
            throw new PreconditionFailedException("L'id n'est pas valide");
        }
        if (reservationEntityOptional.isEmpty()) {
            throw new NotFoundException("L'id n'existe pas");
        }
        ClientReservationEntity entity = reservationEntityOptional.get();
        if (!entity.getClientEntity().getMail().equals(mail)) {
            throw new UnauthorizedException("Cette réservation ne vous appartient pas");
        }
        CompanyConfig companyConfigFind = companyConfigurations
                .stream()
                .filter(companyConfig -> companyConfig.getId() == entity.getCompanyEntity().getId())
                .findFirst().get();
        try {
            reservationRESTRepository.deleteReservation(entity.getReservationId().toString(), companyConfigFind.getUrl());
        } catch (Exception e) {
            throw new InternalServerErrorException("Problème lors de la suppression");
        }
        reservationRepository.deleteById(UUID.fromString(request.getIdReservation()));
        DeleteReservationResponse deleteReservationResponse = new DeleteReservationResponse();
        deleteReservationResponse.setMessage("Succès de la suppression");
        return deleteReservationResponse;
    }
}

