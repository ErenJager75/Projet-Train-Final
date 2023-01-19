package fr.ensiie.endpoint;


import fr.ensiie.model.xml.*;
import fr.ensiie.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.security.NoSuchAlgorithmException;

@Endpoint
@RequiredArgsConstructor
public class TokenEndpoint {

    private static final String NAMESPACE_URI = "http://www.web-service-train-ensiie.com/schema/token";

    private final ClientService clientService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetJWTTokenRequest")
    @ResponsePayload
    public GetJWTTokenResponse getJWTToken(@RequestPayload GetJWTTokenRequest request) throws NoSuchAlgorithmException {
        return clientService.getJWTToken(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CheckJWTTokenRequest")
    @ResponsePayload
    public CheckJWTTokenResponse checkJWTToken(@RequestPayload CheckJWTTokenRequest request) throws NoSuchAlgorithmException {
        return clientService.checkJWTToken(request);
    }

}
