import {Injectable, OnInit} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {map, Observable} from 'rxjs';
import {Client, NgxSoapService} from "ngx-soap";

@Injectable({
  providedIn: 'root'
})
export class RouteGuard implements CanActivate {

  client: Client;
  constructor(private router: Router, private soapService: NgxSoapService) {
  }


  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.client) {
      return this.checkToken();
    }
    this.soapService.createClient('http://localhost:8081/service/tokenWsdl.wsdl').then((client: Client) => {
      this.client = client;
      return this.checkToken();
    }).catch((error) => {
      return false;
    });
  }

  checkToken() {
    return this.client.call('CheckJWTToken', {'JWTtoken': sessionStorage.getItem('token')})
      .pipe(map(reponse => {
        if (reponse?.result?.valid) {
          return true;
        }
        return this.router.parseUrl('/#/login')
      }));
  }


}
