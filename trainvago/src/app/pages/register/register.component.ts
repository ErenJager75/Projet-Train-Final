import {Component, OnInit} from '@angular/core';
import {Client, NgxSoapService} from "ngx-soap";
import {FormBuilder} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  createForm: any;
  client: Client;
  error: string = null;

  constructor(private soapService: NgxSoapService, private formBuilder: FormBuilder, private router: Router) {
  }

  ngOnInit() {
    this.createForm = this.formBuilder.group({
      mail: '',
      lastName: '',
      firstName: '',
      password: ''
    });
    this.soapService.createClient('http://localhost:8081/service/clientWsdl.wsdl').then((client: Client) => {
      this.client = client;
    }).catch((error) => {
      console.error(error);
    });
  }

  createClient(): void {
    const EMAIL_REGEX = /^[\w-\+]+(\.[\w]+)*@[\w-]+(\.[\w]+)*(\.[a-z]{2,})$/;
    const body = this.createForm.value;
    this.error = null;
    if (body.mail === '' || body.lastName === '' || body.firstName === '' || body.password === '') {
      this.error = 'Certains champs ne sont pas remplis.';
      return;
    }
    if (!EMAIL_REGEX.test(body.mail)) {
      this.error = 'L\'adresse mail n\'est pas valide.';
      return;
    }
    this.client.call('CreateClient', body).subscribe(result => {
        window.alert("Compte créé avec succès")
        this.router.navigate(['/login'])
      },
      (err: HttpErrorResponse) => {
        const xmlString = err.error;
        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(xmlString, 'text/xml');
        this.error = xmlDoc.getElementsByTagName('faultstring')[0].textContent;
      })
  }
}
