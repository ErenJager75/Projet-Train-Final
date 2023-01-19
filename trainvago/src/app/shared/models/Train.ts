export class Train {
  constructor(id: any, departure: any, arrival: any, departureDate: Date | undefined, isFlexible: boolean | undefined, classe: string | undefined, numberPlacesAvailable: number | undefined, company: string | undefined) {
    this.id = id;
    this.departure = departure;
    this.arrival = arrival;
    this.departureDate = departureDate;
    this.isFlexible = isFlexible;
    this.classe = classe;
    this.numberPlacesAvailable = numberPlacesAvailable;
    this.company = company;
  }

  id: string;
  departure: string;
  arrival: string;
  departureDate: Date;
  isFlexible: boolean;
  classe: string;
  numberPlacesAvailable: number;
  company: string;
}
