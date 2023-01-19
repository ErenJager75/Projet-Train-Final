package fr.ensiie.controller;


import fr.ensiie.model.Train;
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
@RequestMapping("/trains")
@RequiredArgsConstructor
@Tag(name = "TrainController", description = "L'API TRAIN qui permet de gérer la ressource TRAIN")
public class TrainController {

    private final TrainService trainService;

    @Operation(summary = "Renvoie la liste de tous les Trains où on peut encore réserver",
            description = "Permet d'obtenir la liste de tous les Trains disponibles dans la base de données de la compagnie qui ont encore au moins une place dans n'importe quelle classe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La liste des trains a été trouvée sans erreur",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Train.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseStatusException.class))})
    })
    @GetMapping
    public List<Train> list() {
        return trainService.list();
    }

    @Operation(summary = "Renvoie la liste de tous les Trains où on peut encore réserver qui ont la gare de départ et la gare d'arrivée passée dans le chemin",
            description = "Permet d'obtenir la liste de tous les Trains disponibles dans la base de données de la compagnie qui ont la gare de départ et d'arrivée passées dans le chemin et qui ont encore au moins une place dans n'importe quelle classe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La liste des trains a été trouvée sans erreur",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Train.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseStatusException.class))})
    })
    @GetMapping("departure/{departure}/arrival/{arrival}")
    public List<Train> listByDepartureAndArrival(@Parameter(required = true, description = "La gare de départ du train",
            schema = @Schema(implementation = String.class, defaultValue = "PARIS"))
                                                 @PathVariable String departure,
                                                 @Parameter(required = true, description = "La gare d'arrivée du train",
                                                         schema = @Schema(implementation = String.class, defaultValue = "MARSEILLE"))
                                                 @PathVariable String arrival) {
        return trainService.listByDepartureAndArrival(departure, arrival);
    }

    @Operation(summary = "Renvoie la liste de tous les Trains où on peut encore réserver qui sont à " +
            "la date passée dans le chemin et qui ont pour gare de départ et gare d'arrivée, les deux gares passées dans le chemin",
            description = "Permet d'obtenir la liste de tous les Trains disponibles dans la base de données de la " +
                    "compagnie à la date passée dans le chemin, qui ont pour gare de départ la première gare passée " +
                    "dans le chemin et qui ont pour gare d'arrivée la deuxième gare passée dans le chemin et " +
                    "qui ont encore au moins une place dans n'importe quelle classe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La liste des trains a été trouvée sans erreur",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Train.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseStatusException.class))})
    })
    @GetMapping("departure/{departure}/arrival/{arrival}/date/{date}")
    public List<Train> listByDateAndDepartureAndArrival(@Parameter(required = true, description = "La gare de départ du train",
            schema = @Schema(implementation = String.class, defaultValue = "PARIS"))
                                                        @PathVariable String departure,
                                                        @Parameter(required = true, description = "La gare d'arrivée du train",
                                                                schema = @Schema(implementation = String.class, defaultValue = "MARSEILLE"))
                                                        @PathVariable String arrival,
                                                        @Parameter(required = true, description = "Date de la recherche pour le train",
                                                                schema = @Schema(implementation = Instant.class, defaultValue = "2022-12-27"))
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                        @PathVariable Date date) {
        return trainService.listByDateAndDepartureAndArrival(date.toInstant(), departure, arrival);
    }


    @Operation(summary = "Renvoie la liste de tous les Trains où on peut encore réserver qui sont à la date passée " +
            "dans le chemin et qui ont pour gare de départ et gare d'arrivée, les deux gares passées dans le chemin et " +
            "qui ont encore des places disponibles dans la classe précisée.",
            description = "Permet d'obtenir la liste de tous les Trains disponibles dans la base de données de la " +
                    "compagnie à la date passée dans le chemin, qui ont pour gare de départ la première gare passée " +
                    "dans le chemin et qui ont pour gare d'arrivée la deuxième gare passée dans le chemin et " +
                    "qui ont encore au moins une place dans la classe précisée parmi \"STANDARD\", \"BUSINESS\" et \"FIRST\"")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La liste des trains a été trouvée sans erreur",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Train.class))}),
            @ApiResponse(responseCode = "412", description = "La classe fournie ne fait pas partie des classes disponibles",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseStatusException.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseStatusException.class))})
    })
    @GetMapping("departure/{departure}/arrival/{arrival}/date/{date}/classe/{classe}")
    public List<Train> listByDateAndDepartureAndArrival(@Parameter(required = true, description = "La gare de départ du train",
            schema = @Schema(implementation = String.class, defaultValue = "PARIS"))
                                                        @PathVariable String departure,
                                                        @Parameter(required = true, description = "La gare d'arrivée du train",
                                                                schema = @Schema(implementation = String.class, defaultValue = "MARSEILLE"))
                                                        @PathVariable String arrival,
                                                        @Parameter(required = true, description = "Date de la recherche pour le train",
                                                                schema = @Schema(implementation = Instant.class, defaultValue = "2022-12-27"))
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                        @PathVariable Date date,
                                                        @Parameter(required = true, description = "La classe précise que l'on veut parmi les valeurs: " +
                                                                "\"STANDARD\", \"BUSINESS\" et \"FIRST\"",
                                                                schema = @Schema(implementation = String.class, defaultValue = "STANDARD"))
                                                        @PathVariable String classe) {
        if (classe == null || (!classe.equalsIgnoreCase("STANDARD")
                && !classe.equalsIgnoreCase("BUSINESS")
                && !classe.equalsIgnoreCase("FIRST"))) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "La classe n'existe pas");
        }
        return trainService.listByDateAndDepartureAndArrivalAndClasse(date.toInstant(), departure, arrival, classe);
    }

    @Operation(summary = "Renvoie le Train qui a l'id passé dans le chemin",
            description = "Permet d'obtenir le Train qui a l'id passé dans le chemin, renvoie une erreur 404, si le Train avec cet id n'existe pas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Train trouvé avec succès",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Train.class))}),
            @ApiResponse(responseCode = "404", description = "Le Train avec l'id passé en paramètre n'existe pas dans la base",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseStatusException.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseStatusException.class))})
    })
    @GetMapping("{id}")
    public Train read(@Parameter(required = true, description = "L'id du TRAIN")
                      @PathVariable UUID id) {
        return trainService.read(id);
    }

    @Operation(summary = "Ajoute le train dans la base de données",
            description = "Le train passé dans le corps de la requête est ajouté à la base de données. Pas besoin de fournir un ID, il est ajouté automatiquement par le serveur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Train ajouté avec succès",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Train.class))}),
            @ApiResponse(responseCode = "412", description = "Un des paramètres du train est incorrect",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseStatusException.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseStatusException.class))})
    })
    @PostMapping
    public Train create(@Parameter(required = true, description = "Le train à ajouter")
                        @RequestBody Train train) {
        if (train.getDeparture() == null || train.getDeparture().equals("")) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Il faut fournir une gare de départ");
        }
        if (train.getArrival() == null || train.getArrival().equals("")) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Il faut fournir une gare d'arrivée");
        }
        if (train.getDepartureDate() == null) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Il faut préciser un horaire de départ");
        }
        if (train.getNumberSeatsFirst() < 0 || train.getNumberSeatsStandard() < 0 || train.getNumberSeatsBusiness() < 0) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Le nombre de places dans une classe ne peut pas être négatif");
        }
        if ((train.getNumberSeatsFirst() + train.getNumberSeatsBusiness() + train.getNumberSeatsStandard()) <= 0) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Il faut au moins une place au sein de ce train");
        }
        if (train.getIsFlexible() == null) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Il faut préciser si le train est flexible ou non");
        }
        return trainService.create(train);
    }

}

