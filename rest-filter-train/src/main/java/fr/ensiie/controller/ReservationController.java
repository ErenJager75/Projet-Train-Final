package fr.ensiie.controller;


import fr.ensiie.model.ClasseEnum;
import fr.ensiie.model.Reservation;
import fr.ensiie.model.Train;
import fr.ensiie.service.ReservationService;
import fr.ensiie.service.TrainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "ReservationController", description = "L'API RESERVATION qui permet de gérer la ressource RESERVATION")
public class ReservationController {

    private final ReservationService reservationService;


    @Operation(summary = "Renvoie la Reservation qui a l'id passé dans le chemin",
            description = "Permet d'obtenir la Réservation qui a l'id passé dans le chemin, renvoie une erreur 404, si la Réservation avec cet id n'existe pas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réservation trouvée avec succès",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))}),
            @ApiResponse(responseCode = "404", description = "La Réservation avec l'id passé en paramètre n'existe pas dans la base",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseStatusException.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseStatusException.class))})
    })
    @GetMapping("{id}")
    public Reservation read(@Parameter(required = true, description = "L'ID de la réservation")
                            @PathVariable UUID id) {
        return reservationService.read(id);
    }

    @Operation(summary = "Ajoute la réservation dans la base de données",
            description = "La réservation passée dans le corps de la requête est ajoutée à la base de données. Pas besoin de fournir un ID, il est ajouté automatiquement par le serveur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réservation ajoutée avec succès",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))}),
            @ApiResponse(responseCode = "412", description = "Un des paramètres de la réservation est incorrect",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseStatusException.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseStatusException.class))})
    })
    @PostMapping
    public Reservation create(@Parameter(required = true, description = "La réservation à ajouter")
                              @RequestBody Reservation reservation) {
        if (reservation.getLastName() == null || reservation.getLastName().equals("")) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Il faut fournir un nom de famille");
        }
        if (reservation.getFirstName() == null || reservation.getFirstName().equals("")) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Il faut fournir un prénom");
        }
        if (reservation.getClasse() == null || ClasseEnum.getByName(reservation.getClasse()) == null) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Il faut fournir une classe qui existe");
        }
        if (reservation.getTrain() == null || reservation.getTrain().getId() == null) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Il faut fournir un train avec un id");
        }
        return reservationService.create(reservation);
    }

    @Operation(summary = "Supprime la Reservation qui a l'id passé dans le chemin",
            description = "Supprime la réservation qui a l'id passé dans le chemin et renvoie un code 204 si tout s'est bien passé, renvoie une erreur 404, si la Réservation avec cet id n'existe pas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Réservation supprimée avec succès",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = void.class))}),
            @ApiResponse(responseCode = "404", description = "La Réservation avec l'id passé en paramètre n'existe pas dans la base",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseStatusException.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseStatusException.class))})
    })
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(required = true, description = "L'ID de la réservation")
                            @PathVariable UUID id) {
        reservationService.delete(id);
    }

}

