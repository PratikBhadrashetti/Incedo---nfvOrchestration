import { Component, OnInit } from '@angular/core';
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';

@Component({
  selector: 'app-onlineSupport',
  templateUrl: './onlineSupport.component.html',
  styleUrls: ['./faq.component.css']
})
export class OnlineSupportComponent implements OnInit {
  constructor(private authenticationService: AuthenticationService,
              private statusservice:StatusService) { }
  ngOnInit() {
    this.statusservice.breadcrumb("Online Support");
  }
}
