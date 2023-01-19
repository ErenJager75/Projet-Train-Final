import {Component, OnInit} from '@angular/core';

// core components
import {Train} from "../../shared/models/Train";
import {Client, NgxSoapService} from "ngx-soap";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'bucket',
  templateUrl: './bucket.component.html',
  styleUrls: ['./bucket.component.scss']
})
export class BucketComponent implements OnInit {

  public trains: Train[];

  public client: Client;

  constructor(private soapService: NgxSoapService, private router: Router) {
  }

  ngOnInit() {
    this.trains = JSON.parse(sessionStorage.getItem('trains'));
    this.soapService.createClient('http://localhost:8081/service/trainWsdl.wsdl').then((client: Client) => {
      this.client = client;
    }).catch((error) => {
      console.error(error);
    });
  }


  submitReservations() {
    const body = {
      JWTtoken: sessionStorage.getItem('token'),
      reservationSingle: []
    };
    this.trains.forEach(train => {
      const trainInList = body?.reservationSingle?.find(reservationSingle => reservationSingle?.idTrain === train?.id
      && reservationSingle?.classe === train?.classe && reservationSingle?.company === train?.company);
      if (trainInList) {
        trainInList.numberPlaces += 1;
      } else {
        body.reservationSingle.push({idTrain: train?.id, company: train?.company, classe: train?.classe, numberPlaces: 1})
      }
    })
    this.client.call('MakeReservations', body).subscribe(result => {
        sessionStorage.setItem('trains', JSON.stringify([]));
        window.alert('Succès des réservations')
        this.router.navigate(['/reservations']);
      },
      (err: HttpErrorResponse) => {
        const xmlString = err.error;
        console.log(err);
        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(xmlString, 'text/xml');
        window.alert(xmlDoc.getElementsByTagName('faultstring')[0].textContent);
      })
  }



  deleteTrainFromList(index: number) {
    delete this.trains[index];
    this.trains = this.trains.filter(train => train !== null)
    sessionStorage.setItem('trains', JSON.stringify(this.trains));
  }

}
