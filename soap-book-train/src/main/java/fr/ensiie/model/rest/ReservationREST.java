package fr.ensiie.model.rest;

import fr.ensiie.model.xml.Classe;
import fr.ensiie.model.xml.Company;
import fr.ensiie.model.xml.Reservation;
import fr.ensiie.model.xml.Train;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.Instant;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ReservationREST {

    private UUID id;
    private String lastName;
    private String firstName;
    private String classe;
    private TrainREST train;

    public Reservation toReservationXML(UUID resId,String company) throws DatatypeConfigurationException {
        GregorianCalendar gregorianCalendar = GregorianCalendar
                .from(this.getTrain().getDepartureDate().atZone(ZoneId.of("Europe/Paris")));
        XMLGregorianCalendar departureDate = DatatypeFactory
                .newInstance()
                .newXMLGregorianCalendar(
                        gregorianCalendar
                );

        Reservation reservation = new Reservation();
        reservation.setId(resId.toString());
        Train train = new Train();
        train.setNumberPlacesAvailable(0);
        train.setDepartureDate(departureDate);
        train.setDeparture(this.getTrain().getDeparture());
        train.setArrival(this.getTrain().getArrival());
        train.setClasse(Classe.valueOf(this.classe));
        train.setCompany(Company.valueOf(company.replace(" ", "_")));
        train.setIsFlexible(this.getTrain().getIsFlexible());
        train.setId(this.getTrain().getId().toString());
        reservation.setTrain(train);
        return reservation;
    }

    @Override
    public String toString() {
        return "ReservationREST{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", classe='" + classe + '\'' +
                ", train=" + train +
                '}';
    }
}
