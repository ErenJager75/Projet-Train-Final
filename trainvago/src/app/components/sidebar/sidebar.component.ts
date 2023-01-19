import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

declare interface RouteInfo {
  path: string;
  title: string;
  icon: string;
  class: string;
  hide: boolean;
}

export const ROUTES: RouteInfo[] = [
  {path: '/search', title: 'Recherche Trains', icon: 'ni-bullet-list-67 text-blue', class: '', hide: false},
  {path: '/reservations', title: 'Mes Réservations', icon: 'ni-single-02 text-blue', class: '', hide: false},
  {path: '/bucket', title: 'Votre panier', icon: 'ni-single-02 text-blue', class: '', hide: true},
];

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  public menuItems: any[];
  public isCollapsed = true;

  constructor(private router: Router) {
  }

  ngOnInit() {
    this.menuItems = ROUTES.filter(menuItem => menuItem);
    this.router.events.subscribe((event) => {
      this.isCollapsed = true;
    });
  }
}
