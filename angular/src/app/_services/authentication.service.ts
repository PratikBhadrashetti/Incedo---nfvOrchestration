import { Injectable } from '@angular/core';
import { Http, Headers, Response,URLSearchParams,ResponseOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import {StatusService } from './status.service';
import * as GlobalVariable from "./constants";
import 'rxjs/Rx'

@Injectable()
export class AuthenticationService {
    constructor(private http: Http,private statusservice:StatusService) {}
    
    apiCallGet(url){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+ url,{headers:headers})
                    .map((res: Response) =>{
                        this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            if(url.indexOf('viewvuic')>-1 ||url.indexOf('vim')>-1){
                                return res;
                            }else{
                                return res.json();
                            }
                        }else{
                            return res;
                        }
                        
                    });    
    } 


    deleteApiCall(url){
        return this.http.delete(GlobalVariable.BASE_API_URL+url)
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                           return true;
                        }else{
                            return false;
                        }
                        
                    }); 
    }
    addVnf(body){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'vnf/' , body,{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return "success";
                        }else{
                            return res;
                        }
                        
                    });    
    } 

    associateVnf(urlparam,body){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.patch(GlobalVariable.BASE_API_URL+'catalog/' +urlparam + '/vnfAdd', body,{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                           return "success";
                        }else{
                            return res;
                        }
                        
                    });    
    } 

    catalogPostCall(body){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'catalog', body,{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                           return res.json();
                        }else{
                            return false;
                        }
                        
                    }); 
    }
    catalogPatchCall(body,id){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.patch(GlobalVariable.BASE_API_URL+'catalog/'+id, body,{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                           return true;
                        }else{
                            return false;
                        }
                        
                    }); 
    }
    logoPatchCall(formData,id){
        let headers = new Headers();
        headers.append('Accept', 'application/json');
        return this.http.patch(GlobalVariable.BASE_API_URL+'catalog/'+id+'/logo', formData, { headers: headers })
                .map((res: Response) =>{
                     this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return true;
                        }else{
                            return false;
                        }
                    });  
    }
    userToCatalogPatch(id,body){
        let headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.patch(GlobalVariable.BASE_API_URL+'catalog/'+id+'/user', body, { headers: headers })
                .map((res: Response) =>{
                     this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return true;
                        }else{
                            return false;
                        }
                    });  
    }
    userToVnfPatch(id,body){
        let headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.patch(GlobalVariable.BASE_API_URL+'vnfinstance/'+id+'/user', body, { headers: headers })
                .map((res: Response) =>{
                     this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return true;
                        }else{
                            return false;
                        }
                    });  
    }
    dissociateVnfCall(id,body){
        let headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.patch(GlobalVariable.BASE_API_URL+'catalog/'+id+'/vnf', body, { headers: headers })
                .map((res: Response) =>{
                     this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return true;
                        }else{
                            return false;
                        }
                    });  
    }
    getRegisteredVnf(){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'vnf/' ,{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            var fortest=res.json();
                            return res.json();
                        }else{
                            return res;
                        }
                        
                    });    
    }
    launchVnfInstPost(body){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'vnfinstance', body,{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                           return true;
                           //return res.json();
                        }else{
                            return false;
                        }
                        
                    }); 
    }
    login(user, password){
        return this.http.post(GlobalVariable.BASE_API_URL+'login?user=' + user + '&password=' + password, '')
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                           return true;
                        }else{
                            return false;
                        }
                    }); 
    }
    getUserInfo(){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'user/info',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    });    
    } 

    register(body){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'register', body, {headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res;
                        }else{
                            return res;
                        }
                    });
    }

     userregister(body){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'registerenduser', body, {headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res;
                        }else{
                            return res;
                        }
                    });
    }

    saveTenant(body){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'tenantmanagement',body, {headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res;
                        }else{
                            return res;
                        }
                    });    
    }
     registerTenant(body){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'registertenantadmin',body, {headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res;
                        }else{
                            return res;
                        }
                    });    
    }
    getAllTenant(){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'getalltenants',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    });    
    } 
    getRedirecttoregister(token,email,tenantname){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'redirecttoregister/'+token+"/"+email+"/"+tenantname,{headers:headers})
                    .map((res: Response) =>{
                        // this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    });    
    } 
    getRedirecttosetpassword(token){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'redirecttosetpassword/'+token,{headers:headers})
                    .map((res: Response) =>{
                        // this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    });    
    } 
     forgotPassword(body){
         var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'forgotpassword',body,{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res;
                        }else{
                            return res;
                        }
                    }); 
    }

     setNewPassword(body,token){
         var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'setnewpassword/' +token,body,{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                           return res;
                        }else{
                            return res;
                        }
                    }); 
    }

    setForgotPassword(body,token){
         var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'setforgotpassword/' +token,body,{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                           return res;
                        }else{
                            return res;
                        }
                    }); 
     }

    changePassword(body){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'changepassword', body, {headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res;

                        }else{
                            return res;
                        }
                    });

    }


    resetUser(body,id){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post('/opennfv/resetuser/'+id, body, {headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res;

                        }else{
                            return res;
                        }
                    });

    }
   /* registerVnf(formData){
        let headers = new Headers();
        headers.append('Accept', 'application/json');
        return this.http.post(GlobalVariable.BASE_API_URL+'vnfd', formData, { headers: headers })
                .map((res: Response) =>{
                     this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return true;
                        }else{
                            return false;
                        }
                    });  
    }*/
   imageUploadPost(formData,url){
        let headers = new Headers();
        headers.append('Accept', 'application/json');
        return this.http.post(GlobalVariable.BASE_API_URL+url, formData, { headers: headers })
                .map((res: Response) =>{
                     this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return true;
                        }else{
                            return false;
                        }
                    });  
    }
    getAllUser(){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'getalluser',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    });    
    } 
    addUser(body){
        let headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'adduser',body, { headers: headers })
                .map((res: Response) =>{
                     this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return true;
                        }else{
                            return false;
                        }
                    });  
    }
    assignExternalIp(id){
         let headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.put(GlobalVariable.BASE_API_URL+'vnfinstance/'+id+'/assignExternalIP', { headers: headers })
                .map((res: Response) =>{
                     this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return true;
                        }else{
                            return false;
                        }
                    });  

    }
     savePrivateNetwork(body){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'privatenetwork', body, {headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res;

                        }else{
                            return res;
                        }
                    });

    }
    getAllPrivateNetwork(){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'privatenetwork',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    });    
    } 
     saveTenantProjectInfo(body){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'tenantprojectinfo', body, {headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res;
                        }else{
                            return res;
                        }
                    });
    }
     saveTenantNetwork(body){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'networktenant', body, {headers:headers})
                    .map((res: Response) =>{
                        this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res;
                        }else{
                            return res;
                        }
                    });
    }
     
    vnfRegister(body){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'vnfregister', body, {headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res;
                        }else{
                            return res;
                        }
                    });
    }
      fileDownload(id){
         var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'download/'+id,{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res;
                        }else{
                            return res;
                        }
                    }); 
    }
   
    

    // multi vim code started here
     getVims(){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'vim',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    });    
    }
    postVim(body){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.post(GlobalVariable.BASE_API_URL+'vim', body, {headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res;
                        }else{
                            return res;
                        }
                    });
    }
      deleteVim(id){
        return this.http.delete(GlobalVariable.BASE_API_URL+'vim/'+id)
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                           return true;
                        }else{
                            return false;
                        }
                        
                    }); 
    }
    getTenantVims(id){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'tenant/'+id+'/vim',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    });    
    }
    assignTenantVim(id,body){
        let headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.patch(GlobalVariable.BASE_API_URL+'tenant/'+id+'/vim',body,{ headers: headers })
                .map((res: Response) =>{
                     this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return true;
                        }else{
                            return false;
                        }
                    });  
    }
      getTenantVimsAllocatedIp(vimid,id){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'vim/'+vimid+'/tenant/'+id+'/allocatedip',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    });    
    }
     getTenantUnassignedVims(id){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'tenant/'+id+'/vimunassigned',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    });    
    }
     deAllocatedIP(vimid,tenantid){
        let headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.patch(GlobalVariable.BASE_API_URL+'vim/'+vimid+'/tenant/'+tenantid+'/decallocatedip', { headers: headers })
                .map((res: Response) =>{
                     this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return true;
                        }else{
                            return false;
                        }
                    });  
    }
     incAllocatedIP(vimid,tenantid){
        let headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
       return this.http.patch(GlobalVariable.BASE_API_URL+'vim/'+vimid+'/tenant/'+tenantid+'/incallocatedip', { headers: headers })
                .map((res: Response) =>{
                     this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return true;
                        }else{
                            return false;
                        }
                    });  
    }
    getCatalog(vimid){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'vim/'+vimid+'/catalog',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    });    
    }
    getAllTenantNetwork(vimid){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'vim/'+vimid+'/networktenant',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    });    
    } 
     getVnfCatalog(vimid){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'vim/'+vimid+'/vnfCatalog',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    });    
    }
     getVnfInstance(vimid){
        var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'vim/'+vimid+'/vnfinstance',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    });    
    }
    oshypervisorsDetailGet(vimid){
         var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'vim/'+vimid+'/os-hypervisors/detail',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    }); 
    }
    openstackInstanceLimitGet(vimid,os){
         var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'vim/'+vimid+'/openstackInstance/'+os+'/limits',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    }); 
    }
     openstackInstanceServerDetails(vimid,os){
         var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'vim/'+vimid+'/openstackInstance/'+os+'/servers/details',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
                    }); 
    }
      registervnfd(formData,vimid){
        let headers = new Headers();
        headers.append('Accept', 'application/json');
        return this.http.post(GlobalVariable.BASE_API_URL+'vim/'+vimid+'/vnfd', formData, { headers: headers })
                .map((res: Response) =>{
                     this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return true;
                        }else{
                            return false;
                        }
                    });  
    }
     drmstatitics(){
         var headers = new Headers();
        headers.append('Content-Type', 'application/JSON');
        return this.http.get(GlobalVariable.BASE_API_URL+'drm/statistics',{headers:headers})
                    .map((res: Response) =>{
                         this.statusservice.loadingImg(false);
                        if(res.status === 200){
                            return res.json();
                        }else{
                            return res;
                        }
         }); 
    }
}