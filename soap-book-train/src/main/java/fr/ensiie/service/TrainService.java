package fr.ensiie.service;

import fr.ensiie.config.CompanyConfig;
import fr.ensiie.model.rest.TrainREST;
import fr.ensiie.model.xml.*;
import fr.ensiie.repository.rest.TrainRESTRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainService {

    private final TrainRESTRepository repository;

    private final List<CompanyConfig> companyConfigurations;

    public SearchTrainResponse listAllTrains(Classe classe, Integer numberPlacesWanted) throws DatatypeConfigurationException {
        SearchTrainResponse searchTrainResponse = new SearchTrainResponse();
        List<Train> trains = searchTrainResponse.getTrain();
        for (CompanyConfig companyConfig : companyConfigurations) {
            List<TrainREST> trainRESTS = repository.listAllTrainsByDomainName(companyConfig.getUrl());
            for (TrainREST trainREST : trainRESTS) {
                trains.addAll(convertTrainRESTToTrain(trainREST, companyConfig.getName(), classe, numberPlacesWanted));
            }
        }
        trains.sort((o1, o2) -> o1.getDepartureDate().compare(o2.getDepartureDate()));
        return searchTrainResponse;
    }

    public SearchTrainResponse listAllTrainsByDepartureAndArrival(Classe classe, Integer numberPlacesWanted, String departure, String arrival) throws DatatypeConfigurationException {
        SearchTrainResponse searchTrainResponse = new SearchTrainResponse();
        List<Train> trains = searchTrainResponse.getTrain();
        for (CompanyConfig companyConfig : companyConfigurations) {
            List<TrainREST> trainRESTS = repository.listAllTrainsByDomainNameAndDepartureAndArrival(companyConfig.getUrl(), departure, arrival);
            for (TrainREST trainREST : trainRESTS) {
                trains.addAll(convertTrainRESTToTrain(trainREST, companyConfig.getName(), classe, numberPlacesWanted));
            }
        }
        trains.sort((o1, o2) -> o1.getDepartureDate().compare(o2.getDepartureDate()));
        return searchTrainResponse;
    }

    public SearchTrainResponse listAllTrainsByDepartureAndArrivalAndDepartureDate(Classe classe, Integer numberPlacesWanted, String departure, String arrival, XMLGregorianCalendar date) throws DatatypeConfigurationException {
        SearchTrainResponse searchTrainResponse = new SearchTrainResponse();
        List<Train> trains = searchTrainResponse.getTrain();
        for (CompanyConfig companyConfig : companyConfigurations) {
            List<TrainREST> trainRESTS = repository.listAllTrainsByDomainNameAndDepartureAndArrivalAndDate(companyConfig.getUrl(), departure, arrival, date);
            for (TrainREST trainREST : trainRESTS) {
                trains.addAll(convertTrainRESTToTrain(trainREST, companyConfig.getName(), classe, numberPlacesWanted));
            }
        }
        trains.sort((o1, o2) -> o1.getDepartureDate().compare(o2.getDepartureDate()));
        return searchTrainResponse;
    }

    public SearchTrainResponse listAllTrainsByDepartureAndArrivalAndDepartureDateAndReturnDate(Classe classe,
                                                                                               Integer numberPlacesWanted,
                                                                                               String departure,
                                                                                               String arrival,
                                                                                               XMLGregorianCalendar departureDate,
                                                                                               XMLGregorianCalendar endDate) throws DatatypeConfigurationException {
        SearchTrainResponse searchTrainResponse = new SearchTrainResponse();
        List<Train> trains = searchTrainResponse.getTrain();
        for (CompanyConfig companyConfig : companyConfigurations) {
            List<TrainREST> trainRESTS = repository.listAllTrainsByDomainNameAndDepartureAndArrivalAndDate(companyConfig.getUrl(), departure, arrival, departureDate);
            for (TrainREST trainREST : trainRESTS) {
                trains.addAll(convertTrainRESTToTrain(trainREST, companyConfig.getName(), classe, numberPlacesWanted));
            }
            trainRESTS = repository.listAllTrainsByDomainNameAndDepartureAndArrivalAndDate(companyConfig.getUrl(), arrival, departure, endDate);
            for (TrainREST trainREST : trainRESTS) {
                trains.addAll(convertTrainRESTToTrain(trainREST, companyConfig.getName(), classe, numberPlacesWanted));
            }
        }
        trains.sort((o1, o2) -> o1.getDepartureDate().compare(o2.getDepartureDate()));
        return searchTrainResponse;
    }


    private static List<Train> convertTrainRESTToTrain(TrainREST trainREST, String companyName, Classe classe, int numberPlacesWanted) throws DatatypeConfigurationException {
        GregorianCalendar gregorianCalendar = GregorianCalendar
                .from(trainREST.getDepartureDate().atZone(ZoneId.of("Europe/Paris")));
        XMLGregorianCalendar departureDate = DatatypeFactory
                .newInstance()
                .newXMLGregorianCalendar(
                        gregorianCalendar
                );

        List<Train> allTrainWithClasses = new ArrayList<>();

        if ((classe == null || classe.equals(Classe.BUSINESS)) && trainREST.getNumberSeatsBusiness() > numberPlacesWanted) {
            Train train = new Train();
            train.setId(trainREST.getId().toString());
            train.setDeparture(trainREST.getDeparture());
            train.setArrival(trainREST.getArrival());
            train.setDepartureDate(departureDate);
            train.setClasse(Classe.BUSINESS);
            train.setNumberPlacesAvailable(trainREST.getNumberSeatsBusiness());
            train.setIsFlexible(trainREST.getIsFlexible());
            train.setCompany(Company.fromValue(companyName));
            allTrainWithClasses.add(train);
        }

        if ((classe == null || classe.equals(Classe.STANDARD)) && trainREST.getNumberSeatsStandard() > numberPlacesWanted) {
            Train train = new Train();
            train.setId(trainREST.getId().toString());
            train.setDeparture(trainREST.getDeparture());
            train.setArrival(trainREST.getArrival());
            train.setDepartureDate(departureDate);
            train.setClasse(Classe.STANDARD);
            train.setNumberPlacesAvailable(trainREST.getNumberSeatsStandard());
            train.setIsFlexible(trainREST.getIsFlexible());
            train.setCompany(Company.fromValue(companyName));
            allTrainWithClasses.add(train);
        }

        if ((classe == null || classe.equals(Classe.FIRST)) && trainREST.getNumberSeatsFirst() > numberPlacesWanted) {
            Train train = new Train();
            train.setId(trainREST.getId().toString());
            train.setDeparture(trainREST.getDeparture());
            train.setArrival(trainREST.getArrival());
            train.setDepartureDate(departureDate);
            train.setClasse(Classe.FIRST);
            train.setNumberPlacesAvailable(trainREST.getNumberSeatsFirst());
            train.setIsFlexible(trainREST.getIsFlexible());
            train.setCompany(Company.fromValue(companyName));
            allTrainWithClasses.add(train);
        }


        return allTrainWithClasses;
    }


}
