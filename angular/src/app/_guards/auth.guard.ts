import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import {StatusService } from '../_services/status.service';

@Injectable()
export class AuthGuard implements CanActivate {

    constructor(private router: Router,private statusservice:StatusService) { }

    canActivate() {
        if(this.statusservice.getCurrentUserRole()){
            // logged in so return true
            return true;
        }
        // not logged in so redirect to login page
        this.router.navigate(['/login']);
        return false;
    }
}