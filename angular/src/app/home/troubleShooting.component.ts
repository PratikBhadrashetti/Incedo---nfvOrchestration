import { Component, OnInit } from '@angular/core';
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';

@Component({
  selector: 'app-troubleShooting',
  templateUrl: './troubleShooting.component.html',
  styleUrls: ['./faq.component.css']
})
export class TroubleShootingComponent implements OnInit {
	constructor(private authenticationService: AuthenticationService,
  			  private statusservice:StatusService) { }

  	ngOnInit() {
  		this.statusservice.breadcrumb("Troubleshooting");
  	}
}
