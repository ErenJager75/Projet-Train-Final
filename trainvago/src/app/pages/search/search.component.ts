import {Component, OnInit} from '@angular/core';
import {Client, NgxSoapService} from "ngx-soap";
import {Train} from "../../shared/models/Train";
import {FormBuilder} from "@angular/forms";

@Component({
  selector: 'search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  client: Client;

  trains: Train[] = null;

  numberPages: number = 0;
  indexCurrentPage: number = 0;
  searchForm: any;
  error: string = null;

  constructor(private soapService: NgxSoapService, private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.searchForm = this.formBuilder.group({
      departure: '',
      arrival: '',
      departureDate: new Date(),
      returnDate: '',
      classe: '',
      numberPlacesWanted: 1
    })
    this.soapService.createClient('http://localhost:8081/service/trainWsdl.wsdl').then((client: Client) => {
      this.client = client;
    }).catch((error) => {
      console.error(error);
    });
  }

  changePage(indexCurrentPage: number) {
    if (indexCurrentPage >= 0 && indexCurrentPage <= this.numberPages - 1) {
      this.indexCurrentPage = indexCurrentPage;
    }

  }

  validForm() {
    const body = this.searchForm.value;
    this.error = null;
    if (body.departure === '' || body.arrival === '') {
      this.error = 'Il faut au moins préciser une ville de départ et une ville d\'arrivée.';
      return;
    }
    if (body.returnDate !== '' && body.departureDate === '') {
      this.error = 'Il faut préciser une date de départ quand on envoie une date de retour.';
      return;
    }
    if (body.numberPlacesWanted < 1) {
      this.error = 'Le nombre de places demandées doit être supérieur ou égal à 1.';
      return;
    }
    this.client.call('SearchTrain', body).subscribe(soapResponse => {
      this.trains = [];
      soapResponse?.result?.train?.forEach(train => {
        this.trains.push(new Train(train?.id, train?.departure, train?.arrival, train?.departureDate, train?.isFlexible, train?.classe, train?.numberPlacesAvailable, train?.company))
      })
      this.indexCurrentPage = 0;
      this.numberPages = Math.floor(this.trains?.length / 5) + 1;
    })
  }

  addToTrainList(train): void {
    const stringSessionStorage = sessionStorage.getItem('trains');
    let trainsToSave = []
    if (stringSessionStorage != null && stringSessionStorage !== '') {
      trainsToSave = JSON.parse(stringSessionStorage);
    }
    trainsToSave.push(train);
    sessionStorage.setItem('trains', JSON.stringify(trainsToSave));
  }

  isMaxReservation(train: Train) {


    const trainsInSession = JSON.parse(sessionStorage.getItem('trains'));

    return trainsInSession?.filter(trainLoop => trainLoop?.id === train?.id
      && trainLoop?.classe === train?.classe && trainLoop?.company === train?.company)?.length === train?.numberPlacesAvailable;

  }
}
