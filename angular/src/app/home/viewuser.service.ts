import { Injectable } from '@angular/core';
import { Http, Headers, Response,URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'

@Injectable()
export class viewUserService {
    constructor(private http: Http) { }
getUsers(){
	return this.http.get("/app/home/users.json")
	.map((response: Response) => {
		// alert(response.json()[0].name);
		return response.json();
	});
}
}
