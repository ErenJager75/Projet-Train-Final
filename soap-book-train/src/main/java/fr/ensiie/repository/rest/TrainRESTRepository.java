package fr.ensiie.repository.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.ensiie.model.rest.TrainREST;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TrainRESTRepository {


    private final RestTemplate restTemplate;

    public List<TrainREST> listAllTrainsByDomainName(String domainName) {
        return sendRequestAndGetResults("http://" + domainName + ":8080/trains");
    }

    public List<TrainREST> listAllTrainsByDomainNameAndDepartureAndArrival(String domainName, String departure, String arrival) {
        return sendRequestAndGetResults("http://" + domainName + ":8080/trains/departure/" + departure.toUpperCase() + "/arrival/" + arrival.toUpperCase());
    }

    public List<TrainREST> listAllTrainsByDomainNameAndDepartureAndArrivalAndDate(String domainName, String departure, String arrival, XMLGregorianCalendar date) {
        return sendRequestAndGetResults("http://" + domainName + ":8080/trains/departure/" + departure.toUpperCase() + "/arrival/" + arrival.toUpperCase() + "/date/" + date.toString());
    }

    private List<TrainREST> sendRequestAndGetResults(String url) {
        ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.convertValue(response.getBody(), new TypeReference<>() {
        });
    }
}
