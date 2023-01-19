import {Routes} from '@angular/router';

import {BucketComponent} from '../../pages/bucket/bucket.component';
import {ListReservationsComponent} from '../../pages/list-reservations/list-reservations.component';
import {SearchComponent} from '../../pages/search/search.component';
import {RouteGuard} from "./route.guard";

export const AdminLayoutRoutes: Routes = [
  {path: 'reservations', component: ListReservationsComponent, canActivate: [RouteGuard]},
  {path: 'search', component: SearchComponent, canActivate: [RouteGuard]},
  {path: 'bucket', component: BucketComponent, canActivate: [RouteGuard]}
];
