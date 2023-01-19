import {Component, OnInit} from '@angular/core';
import {Client, NgxSoapService} from "ngx-soap";
import {Reservation} from "../../shared/models/Reservation";
import {Train} from "../../shared/models/Train";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'list-reservations',
  templateUrl: './list-reservations.component.html',
  styleUrls: ['./list-reservations.component.scss']
})
export class ListReservationsComponent implements OnInit {

  client: Client;
  reservations: Reservation[];

  constructor(private soapService: NgxSoapService) {
  }

  ngOnInit() {
    const body = {
      JWTtoken: sessionStorage.getItem('token')
    };
    this.soapService.createClient('http://localhost:8081/service/trainWsdl.wsdl').then((client: Client) => {
      this.client = client;
      this.client.call('GetReservations', body).subscribe(soapResponse => {
        this.reservations = [];
        soapResponse?.result?.reservation?.forEach(reservation => {
          this.reservations.push(new Reservation(reservation?.id, reservation?.train))
        })
      })
    }).catch((error) => {
      console.error(error);
    });
  }

  deleteReservation(id: string) {
    const body = {
      JWTtoken: sessionStorage.getItem('token'),
      idReservation: id
    };
    this.client.call('DeleteReservation', body).subscribe(soapResponse => {
        this.reservations = this.reservations.filter(reservation => id !== reservation?.id)
      },
      (err: HttpErrorResponse) => {
        const xmlString = err.error;
        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(xmlString, 'text/xml');
        window.alert(xmlDoc.getElementsByTagName('faultstring')[0].textContent);
      })
  }
}
