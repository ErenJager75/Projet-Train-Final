package fr.ensiie.endpoint;


import fr.ensiie.exception.UnauthorizedException;
import fr.ensiie.model.xml.*;
import fr.ensiie.service.ReservationService;
import fr.ensiie.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;

@Endpoint
@RequiredArgsConstructor
public class ReservationEndpoint {

    private static final String NAMESPACE_URI = "http://www.web-service-train-ensiie.com/schema/train";

    private final ReservationService reservationService;

    private final JWTUtil jwtUtil;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "MakeReservationsRequest")
    @ResponsePayload
    public MakeReservationsResponse makeReservations(@RequestPayload MakeReservationsRequest request) throws DatatypeConfigurationException {
        if (!jwtUtil.validateToken(request.getJWTtoken())) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à faire cette action");
        }
        return reservationService.create(request);

    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetReservationsRequest")
    @ResponsePayload
    public GetReservationsResponse getReservations(@RequestPayload GetReservationsRequest request) throws DatatypeConfigurationException {
        if (!jwtUtil.validateToken(request.getJWTtoken())) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à faire cette action");
        }
        return reservationService.findAll(request);

    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteReservationRequest")
    @ResponsePayload
    public DeleteReservationResponse deleteReservation(@RequestPayload DeleteReservationRequest request) throws DatatypeConfigurationException {
        if (!jwtUtil.validateToken(request.getJWTtoken())) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à faire cette action");
        }
        return reservationService.delete(request);

    }



}
