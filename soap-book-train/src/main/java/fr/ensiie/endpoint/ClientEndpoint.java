package fr.ensiie.endpoint;


import fr.ensiie.model.xml.*;
import fr.ensiie.service.ClientService;
import fr.ensiie.service.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import java.security.NoSuchAlgorithmException;

@Endpoint
@RequiredArgsConstructor
public class ClientEndpoint {

    private static final String NAMESPACE_URI = "http://www.web-service-train-ensiie.com/schema/client";

    private final ClientService clientService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateClientRequest")
    @ResponsePayload
    public CreateClientResponse createClient(@RequestPayload CreateClientRequest request) throws NoSuchAlgorithmException {
        return clientService.saveClient(request);
    }

}
