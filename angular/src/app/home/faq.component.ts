import { Component, OnInit } from '@angular/core';
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';

@Component({
  selector: 'app-faq',
  templateUrl: './faq.component.html',
  styleUrls: ['./faq.component.css']
})
export class FaqComponent implements OnInit {
	currentUserRole;
  constructor(private authenticationService: AuthenticationService,
  			  private statusservice:StatusService) { }

  ngOnInit() {
  	this.statusservice.breadcrumb("Frequently Asked Questions");
  	this.currentUserRole = this.statusservice.getCurrentUserRole();
  }

}
