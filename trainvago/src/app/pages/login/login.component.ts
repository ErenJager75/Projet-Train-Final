import { Component, OnInit, OnDestroy } from '@angular/core';
import {Client, NgxSoapService} from "ngx-soap";
import {FormBuilder} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: any;
  client: Client;
  error: string = null;

  constructor(private soapService: NgxSoapService, private formBuilder: FormBuilder, private router: Router) {
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      mail: '',
      password: ''
    });
    this.soapService.createClient('http://localhost:8081/service/tokenWsdl.wsdl').then((client: Client) => {
      this.client = client;
    }).catch((error) => {
      console.error(error);
    });
  }

  getJWTToken(): void {
    const EMAIL_REGEX = /^[\w-\+]+(\.[\w]+)*@[\w-]+(\.[\w]+)*(\.[a-z]{2,})$/;
    const body = this.loginForm.value;
    this.error = null;
    if (body.mail === '' || body.password === '') {
      this.error = 'Certains champs ne sont pas remplis.';
      return;
    }
    if (!EMAIL_REGEX.test(body.mail)) {
      this.error = 'L\'adresse mail n\'est pas valide.';
      return;
    }
    this.client.call('GetJWTToken', body).subscribe(result => {
        sessionStorage.setItem('token', result?.result?.JWTtoken);
        this.router.navigate(['/search']);
      },
      (err: HttpErrorResponse) => {
        const xmlString = err.error;
        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(xmlString, 'text/xml');
        this.error = xmlDoc.getElementsByTagName('faultstring')[0].textContent;
      })
  }

}
