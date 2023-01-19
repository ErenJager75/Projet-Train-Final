package fr.ensiie.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "CLIENT_RESERVATION")
public class ClientReservationEntity {

    @Id
    @Column(name = "ID")
    private UUID id;

    @JoinColumn(name = "CLIENT_MAIL")
    @OneToOne
    private ClientEntity clientEntity;

    @Column(name = "RESERVATION_ID")
    private UUID reservationId;

    @JoinColumn(name = "COMPANY_ID")
    @OneToOne
    private CompanyEntity companyEntity;

    @Override
    public String toString() {
        return "ClientReservationEntity{" +
                "id=" + id +
                ", clientEntity=" + clientEntity +
                ", reservationId=" + reservationId +
                ", companyEntity=" + companyEntity +
                '}';
    }
}
