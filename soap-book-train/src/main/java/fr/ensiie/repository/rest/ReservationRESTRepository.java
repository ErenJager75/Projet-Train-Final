package fr.ensiie.repository.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.ensiie.model.rest.ReservationREST;
import fr.ensiie.model.rest.TrainREST;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationRESTRepository {


    private final RestTemplate restTemplate;

    public ReservationREST postReservationREST(ReservationREST reservationREST, String domainName) throws Exception {
        ReservationREST reservationRESTSaved;
        try {
            reservationRESTSaved = restTemplate.postForObject("http://" + domainName + ":8080/reservations",
                    reservationREST, ReservationREST.class);
        } catch (Exception e) {
            throw e;
        }
        return reservationRESTSaved;
    }

    public ReservationREST getReservationREST(UUID reservationId, String domainName) {
        ReservationREST reservationREST = null;
        try {
            reservationREST = restTemplate.getForObject("http://" + domainName + ":8080/reservations/" + reservationId.toString(),
                    ReservationREST.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return reservationREST;
    }

    public void deleteReservation(String idReservation, String domainName) {
        try {
            restTemplate.delete("http://" + domainName + ":8080/reservations/" + idReservation);
        } catch (Exception e) {
            throw e;
        }
    }
}
