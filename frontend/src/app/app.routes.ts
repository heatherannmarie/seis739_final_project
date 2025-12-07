import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { ParentViewComponent } from './parent-view/parent-view.component';
import { ChildViewComponent } from './child-view/child-view.component';
import { ChoreListComponent } from './chore-list/chore-list.component';
import { StorefrontComponent } from './storefront/storefront.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'parent', component: ParentViewComponent },
    { path: 'child', component: ChildViewComponent },
    { path: 'chores', component: ChoreListComponent },
    { path: 'store', component: StorefrontComponent }
];