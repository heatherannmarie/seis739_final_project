import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';

import { ParentViewComponent } from './parent-view/parent-view.component';
import { ChildViewComponent } from './child-view/child-view.component';
import { ChoreListComponent } from './chore-list/chore-list.component';
import { StorefrontComponent } from './storefront/storefront.component';
import { ParentLoginComponent } from './parent-login/parent-login.component';
import { ParentRegisterComponent } from './parent-register/parent-register.component';
import { ChildLoginComponent } from './child-login/child-login.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'parentdashboard', component: ParentViewComponent },
    { path: 'child', component: ChildViewComponent },
    { path: 'chores', component: ChoreListComponent },
    { path: 'store', component: StorefrontComponent },
    { path: 'parentlogin', component: ParentLoginComponent },
    { path: 'register', component: ParentRegisterComponent },
    { path: 'childlogin', component: ChildLoginComponent }
];