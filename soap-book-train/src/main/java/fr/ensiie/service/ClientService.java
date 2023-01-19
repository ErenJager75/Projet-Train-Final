package fr.ensiie.service;

import fr.ensiie.entity.ClientEntity;
import fr.ensiie.exception.ConflictException;
import fr.ensiie.exception.NotFoundException;
import fr.ensiie.exception.PreconditionFailedException;
import fr.ensiie.exception.UnauthorizedException;
import fr.ensiie.model.xml.*;
import fr.ensiie.repository.db.ClientRepository;
import fr.ensiie.utils.JWTUtil;
import fr.ensiie.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;

    private final JWTUtil jwtUtil;

    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    public CreateClientResponse saveClient(CreateClientRequest request) throws NoSuchAlgorithmException {
        if (request.getMail() == null || request.getMail().equals("")
                || request.getPassword() == null || request.getPassword().equals("")
                || request.getFirstName() == null || request.getFirstName().equals("")
                || request.getLastName() == null || request.getLastName().equals("")) {
            throw new PreconditionFailedException("Certains champs ne sont pas remplis");
        }
        Matcher matcher = pattern.matcher(request.getMail());
        if (!matcher.matches()) {
            throw new PreconditionFailedException("L'adresse mail n'est pas valide");
        }
        Optional<ClientEntity> clientEntityOptional = repository.findById(request.getMail());
        if (clientEntityOptional.isPresent()) {
            throw new ConflictException("Le login est déjà pris");
        }
        repository.save(new ClientEntity().setFirstName(request.getFirstName())
                .setMail(request.getMail())
                .setPassword(PasswordUtils.cryptPassword(request.getPassword()))
                .setLastName(request.getLastName()));
        CreateClientResponse clientResponse = new CreateClientResponse();
        clientResponse.setResponse("Succès de la création");
        return clientResponse;
    }

    public GetJWTTokenResponse getJWTToken(GetJWTTokenRequest request) throws NoSuchAlgorithmException {
        if (request.getMail() == null || request.getMail().equals("")
                || request.getPassword() == null || request.getPassword().equals("")) {
            throw new PreconditionFailedException("Certains champs ne sont pas remplis");
        }
        Optional<ClientEntity> clientEntityOptional = repository.findById(request.getMail());
        if (clientEntityOptional.isEmpty()) {
            throw new NotFoundException("Ce login n'existe pas");
        }
        ClientEntity client = clientEntityOptional.get();
        if (!client.getPassword().equals(PasswordUtils.cryptPassword(request.getPassword()))) {
            throw new UnauthorizedException("Mot de passe incorrect");
        }
        GetJWTTokenResponse getJWTTokenResponse = new GetJWTTokenResponse();
        getJWTTokenResponse.setJWTtoken(jwtUtil.generateToken(client));
        return getJWTTokenResponse;
    }

    public CheckJWTTokenResponse checkJWTToken(CheckJWTTokenRequest request) {
        CheckJWTTokenResponse checkJWTTokenResponse = new CheckJWTTokenResponse();
        checkJWTTokenResponse.setValid(jwtUtil.validateToken(request.getJWTtoken()));
        return checkJWTTokenResponse;
    }
}
