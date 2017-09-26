import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders} from '@angular/core';
import { HomeComponent } from './home/index';
import { LoginComponent } from './login/index';
import { AuthGuard } from './_guards/index';
import { CatalogueComponent} from './home/catalogue.component';
import { UserComponent} from './home/user.component';
import { CatalogueDetailComponent } from './home/cataloguedetail.component';
import { VnfComponent } from './home/vnf.component';
import { addCatalogueComponent} from './home/addcatalogue.component';
import { modifyCatalogueComponent} from './home/modifycatalogue.component';
import { VnfListComponent } from './home/vnflist.component';
import { addVnfComponent } from './home/addvnf.component';
import { viewUserComponent } from './home/viewuser.component';
import { addUserComponent } from './home/adduser.component';
import { myAccountComponent } from './home/myaccount.component';
import { vnfManagementComponent } from './home/vnfmanagement.component';
import { RegisterComponent } from './register/index';
import { ChangepasswordComponent } from './home/changepassword.component';
import { FaqComponent } from './home/faq.component';
import { ContactusComponent } from './home/contactus.component';
import { ResetuserComponent } from './home/resetuser.component';
import { StatusComponent } from './home/status.component';
import { OnlineSupportComponent } from './home/onlineSupport.component';
import { TroubleShootingComponent } from './home/troubleShooting.component';
import { RegisterVnfComponent } from './home/registerVnf.component';
import { ImageUploadComponent } from './home/imageUpload.component';
import { TenantmanagementComponent } from './home/tenantmanagement.component';
import { RedirecttoregisterComponent } from './redirecttoregister/redirecttoregister.component';
import { RedirecttosetpasswordComponent } from './redirecttosetpassword/index';
import { ForgotpasswordComponent } from './forgotpassword/forgotpassword.component';
import { RedirecttoforgotpasswordComponent } from './redirecttoforgotpassword/redirecttoforgotpassword.component';
import { DashComponent } from './home/Admin_dashboard/dashboard.component';
import { UsermanagementComponent } from './home/usermanagement.component';
import { RedirecttoadduserComponent } from './redirecttoadduser/redirecttoadduser.component';
import { NetworkComponent } from './home/network.component';
import { PrivatenetworkComponent } from './home/privatenetwork.component';
import { ProjectinfoComponent } from './home/projectinfo.component';
import { ManagevimsComponent } from './home/managevims.component';
import { DrmstatiticsComponent } from './home/drmstatitics.component';
const appRoutes: Routes = [
    { path: 'home', component: HomeComponent,canActivate: [AuthGuard],
        children: [
            { path:'',component:CatalogueComponent},
            { path:'catalogue',component:CatalogueComponent,data:{breadcrumb: ' Catalogs'}},
            { path:'vnfmanagement',component:vnfManagementComponent,data:{breadcrumb:' VNF Management'}},
            { path:'user',component:UserComponent,data:{breadcrumb:'VNF MANAGEMENT'}},
            { path:'detail',component:CatalogueDetailComponent,data:{breadcrumb:'Catalogs / Catalog Detail'}},
            { path:'vnf',component:VnfComponent,data:{breadcrumb:' VNF Instances'}},
            { path:'addcatalogue',component:addCatalogueComponent,data:{breadcrumb:' Add Catalog'}},
            { path:'modifycatalogue',component:modifyCatalogueComponent,data:{breadcrumb:' Modify Catalog'}},
            { path:'vnflist',component:VnfListComponent,data:{breadcrumb:' VNF List'}},
            { path:'addvnf',component:addVnfComponent,data:{breadcrumb:'  Associate VNF'}},
            { path:'viewuser',component:viewUserComponent,data:{breadcrumb:'User Management / User Provisioning'}},
            { path:'adduser',component:addUserComponent,data:{breadcrumb:'Add User'}},
            { path:'faq',component:FaqComponent},
            { path:'changepassword',component:ChangepasswordComponent},
            { path:'contactus',component:ContactusComponent},
            { path:'resetuser',component:ResetuserComponent},
            { path:'status',component:StatusComponent},
            { path:'onlineSupport',component:OnlineSupportComponent},
            { path:'troubleShooting',component:TroubleShootingComponent},
            { path:'registerVnf',component:RegisterVnfComponent},
            { path: 'imageUpload',component:ImageUploadComponent},
            { path: 'dashboard', component: DashComponent },
            { path:'tenantmanagement',component:TenantmanagementComponent},
            { path:'usermanagement',component:UsermanagementComponent},
            { path:'network',component:NetworkComponent},
            { path:'tenantnetwork',component:PrivatenetworkComponent},
            { path:'projectinfo',component:ProjectinfoComponent},
            { path:'managevims',component:ManagevimsComponent},
            { path:'drmstatics',component:DrmstatiticsComponent}
        ]
	},
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'redirecttosetpassword/:token', component: RedirecttosetpasswordComponent },
    { path: 'redirecttoregister/:token/:ptemail/:tenantname', component: RedirecttoregisterComponent },
    { path: 'forgotpassword', component: ForgotpasswordComponent },
    { path: 'redirecttoforgotpassword/:token', component: RedirecttoforgotpasswordComponent },
    { path: 'redirecttoadduser/:token/:ptemail/:tenantname', component: RedirecttoadduserComponent },
    // { path: 'hero', component: HttpExample},
    // { path: 'register', component: RegisterComponent },
    { path: '**', redirectTo: 'home' }
];

export const routing = RouterModule.forRoot(appRoutes,{useHash: true});