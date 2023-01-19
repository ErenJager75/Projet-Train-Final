package fr.ensiie.endpoint;


import fr.ensiie.model.xml.*;
import fr.ensiie.service.TrainService;
import fr.ensiie.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;

@Endpoint
@RequiredArgsConstructor
public class TrainEndpoint {

    private static final String NAMESPACE_URI = "http://www.web-service-train-ensiie.com/schema/train";

    private final TrainService trainService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SearchTrainRequest")
    @ResponsePayload
    public SearchTrainResponse searchTrain(@RequestPayload SearchTrainRequest request) throws DatatypeConfigurationException {
        if (request.getDeparture() != null
                && request.getArrival() != null
                && !request.getDeparture().equals("")
                && !request.getArrival().equals("")) {
            if (request.getDepartureDate() != null) {
                if (request.getReturnDate() != null) {
                    return trainService.listAllTrainsByDepartureAndArrivalAndDepartureDateAndReturnDate(request.getClasse(),
                            request.getNumberPlacesWanted(),
                            request.getDeparture(),
                            request.getArrival(),
                            request.getDepartureDate(),
                            request.getReturnDate());
                }
                return trainService.listAllTrainsByDepartureAndArrivalAndDepartureDate(request.getClasse(),
                        request.getNumberPlacesWanted(),
                        request.getDeparture(),
                        request.getArrival(),
                        request.getDepartureDate());
            }
            return trainService.listAllTrainsByDepartureAndArrival(request.getClasse(), request.getNumberPlacesWanted(), request.getDeparture(), request.getArrival());
        }

        return trainService.listAllTrains(request.getClasse(), request.getNumberPlacesWanted());
    }

}
