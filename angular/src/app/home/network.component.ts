import {Component,OnInit} from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import 'rxjs/add/operator/switchMap';
import {AuthenticationService } from '../_services/index';
import {StatusService } from '../_services/status.service';
@Component({
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.css']
})
export class NetworkComponent implements OnInit {

private userOptions =[];loading = false;passworderror=false;getallprivateNetwork;
statusMsg;model: any = {};userId: any = {};
  constructor(private authenticationService: AuthenticationService,
  			  private statusservice:StatusService,private router: Router) { }

  ngOnInit() {
  	this.statusservice.breadcrumb("Private Network Resources");
    this.authenticationService.getAllPrivateNetwork().subscribe(
          (res) =>{
             this.getallprivateNetwork=res;
          },
          (error)=>{
                this.statusMsg=error.json().error;
          });
  
  
  }
  savePrivateNetwork(){
 
          var body = JSON.stringify({
                      
              "cidr" : this.model.cidr,
              "start": this.model.start,
              "end": this.model.end,
              "dns": this.model.dns,
              "gateway":this.model.gateway

          });
          this.authenticationService.savePrivateNetwork(body).subscribe(
            (res)=>{
              if(res.status==200){
                  this.authenticationService.getAllPrivateNetwork().subscribe(
                    (res) =>{
                      this.getallprivateNetwork=res;
                    },
                    (error)=>{
                          this.statusMsg=error.json().error;
                    });
              }
            
            },
            
            (err)=>{
                  //Handle logout failure}
                  this.statusMsg=err.json().error;
                }
            )
          this.model={};   
  }
  hideStatusMsg(){
        	this.statusMsg=false;
  }
}
