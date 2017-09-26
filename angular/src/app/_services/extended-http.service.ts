import { Injectable} from '@angular/core';
import { Request, XHRBackend, RequestOptions, Response, Http, RequestOptionsArgs, Headers } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import { Router } from '@angular/router';
import {StatusService } from './status.service';

@Injectable()
export class AuthenticatedHttpService extends Http {
  statusMsg;
  constructor(backend: XHRBackend, defaultOptions: RequestOptions,
              private router:Router,private statusservice:StatusService) {
    super(backend, defaultOptions);
  }

  request(url: string | Request, options?: RequestOptionsArgs): Observable<Response> {
    this.statusservice.loadingImg(true);
    return super.request(url, options).catch((error: Response) => {
            this.statusservice.loadingImg(false);
            if (error.status === 401){
                this.statusMsg=error.json().error;
                this.statusservice.setStatus(this.statusMsg);
                this.router.navigateByUrl('/login');
            }else if(error.status === 0){
              this.statusMsg="Server cannot be reached";
              this.statusservice.setStatus(this.statusMsg);
              this.router.navigateByUrl('/login');
            }
            return Observable.throw(error);
        });
}
}