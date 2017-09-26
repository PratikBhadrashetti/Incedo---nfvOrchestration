import { Component, OnInit } from '@angular/core';
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';

@Component({
  selector: 'app-contactus',
  templateUrl: './contactus.component.html',
  styleUrls: ['./contactus.component.css']
})
export class ContactusComponent implements OnInit {

   constructor(private authenticationService: AuthenticationService,
  			   private statusservice:StatusService) { }

  ngOnInit() {
  	this.statusservice.breadcrumb("Contact Us");
  }

}
