import {Train} from "./Train";

export class Reservation {
  constructor(id: any, train: any) {
    this.id = id;
    this.train = train;
  }

  id: string;
  train: Train;
}
