import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule,Http } from '@angular/http';
import { SelectModule } from './@bromzh/ng2-select/src/index';
import { Ng2PaginationModule} from 'ng2-pagination';
import { AppComponent } from './app.component';
import { routing }        from './app.routing';
import { AuthGuard } from './_guards/index';
import { AuthenticationService} from './_services/index';
import { viewUserService } from './home/viewuser.service';
import { HomeComponent } from './home/index';
import { LoginComponent } from './login/index';
import { UserComponent} from './home/user.component';
import { CatalogueComponent} from './home/catalogue.component';
import { CatalogueDetailComponent } from './home/cataloguedetail.component';
import { VnfComponent} from './home/vnf.component';
import { addCatalogueComponent} from './home/addcatalogue.component';
import { modifyCatalogueComponent} from './home/modifycatalogue.component';
import { VnfListComponent } from './home/vnflist.component';
import { addVnfComponent } from './home/addvnf.component';
import { viewUserComponent } from './home/viewuser.component';
import { addUserComponent } from './home/adduser.component';
import { myAccountComponent } from './home/myaccount.component';
import { vnfManagementComponent} from './home/vnfmanagement.component';
import { StatusService } from './_services/status.service';
import { RegisterComponent } from './register/index';
import { ChangepasswordComponent } from './home/changepassword.component';
import { AuthenticatedHttpService} from './_services/extended-http.service';
import { FaqComponent } from './home/faq.component';
import { ContactusComponent } from './home/contactus.component';
import { ResetuserComponent } from './home/resetuser.component';
import { StatusComponent } from './home/status.component';
import { OnlineSupportComponent } from './home/onlineSupport.component';
import { TroubleShootingComponent } from './home/troubleShooting.component';
import { RegisterVnfComponent } from './home/registerVnf.component';
import { TenantmanagementComponent } from './home/tenantmanagement.component';
import { RedirecttoregisterComponent } from './redirecttoregister/redirecttoregister.component';
import { RedirecttosetpasswordComponent } from './redirecttosetpassword/index';
import { ImageUploadComponent } from './home/imageUpload.component';
import { ForgotpasswordComponent } from './forgotpassword/forgotpassword.component';
import { DashComponent } from './home/Admin_dashboard/dashboard.component';
/*import { ChartsModule } from 'ng2-charts/ng2-charts';
*/import { RedirecttoforgotpasswordComponent } from './redirecttoforgotpassword/redirecttoforgotpassword.component';
import { UsermanagementComponent } from './home/usermanagement.component';
import { RedirecttoadduserComponent } from './redirecttoadduser/redirecttoadduser.component';
import { TagInputModule } from 'ng2-tag-input';
import { NetworkComponent } from './home/network.component';
import { Ng2Bs3ModalModule } from 'ng2-bs3-modal/ng2-bs3-modal';
import { PrivatenetworkComponent } from './home/privatenetwork.component';
import { ProjectinfoComponent } from './home/projectinfo.component';
import { ManagevimsComponent } from './home/managevims.component';
import { MyFilterPipe } from './_services/index';
import { FileSizePipe } from './_services/index';
import { UtilityService } from './_services/utility';
import { DrmstatiticsComponent } from './home/drmstatitics.component';
@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    UserComponent,
    CatalogueComponent,
    CatalogueDetailComponent,
    VnfComponent,
    addCatalogueComponent,
    modifyCatalogueComponent,
    VnfListComponent,    
    addVnfComponent,
    viewUserComponent,
    addUserComponent,
    myAccountComponent,
    vnfManagementComponent,
    RegisterComponent,
    ChangepasswordComponent,
    FaqComponent,
    ContactusComponent,
    ResetuserComponent,
    StatusComponent,
    OnlineSupportComponent,
    TroubleShootingComponent,
    RegisterVnfComponent,
    TenantmanagementComponent,
    RedirecttosetpasswordComponent,
    RedirecttoregisterComponent,
    ImageUploadComponent,
    ForgotpasswordComponent,
    RedirecttoforgotpasswordComponent,
    DashComponent,
    UsermanagementComponent,
    RedirecttoadduserComponent,
    NetworkComponent,
    PrivatenetworkComponent,
    ProjectinfoComponent,
    MyFilterPipe,
    FileSizePipe,
    ManagevimsComponent,
    DrmstatiticsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    routing,
    SelectModule,
    Ng2PaginationModule,
    TagInputModule,
    Ng2Bs3ModalModule
    
  ],
  providers: [
    viewUserService,
    AuthGuard,
    AuthenticationService,
    viewUserService,
    StatusService,
    UtilityService,
    { provide: Http, useClass: AuthenticatedHttpService}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
