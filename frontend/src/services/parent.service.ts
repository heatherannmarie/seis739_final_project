import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
    Parent,
    Child,
    Chore,
    StoreItem,
    Transaction,
    CreateParentRequest,
    CreateChildRequest,
    CreateChoreRequest,
    CreateStoreItemRequest,
    PayChildRequest
} from '../models';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class ParentService {
    private http = inject(HttpClient);
    private baseUrl = `${environment.apiUrl}/parents`;

    // Create a new parent account
    createParent(request: CreateParentRequest): Observable<Parent> {
        return this.http.post<Parent>(this.baseUrl, request);
    }

    // Get parent by ID
    getParent(parentId: string): Observable<Parent> {
        return this.http.get<Parent>(`${this.baseUrl}/${parentId}`);
    }

    // Create a child account under a parent
    createChild(parentId: string, request: CreateChildRequest): Observable<Child> {
        return this.http.post<Child>(`${this.baseUrl}/${parentId}/children`, request);
    }

    // Get all children for a parent
    getChildren(parentId: string): Observable<Child[]> {
        return this.http.get<Child[]>(`${this.baseUrl}/${parentId}/children`);
    }

    // Add a new chore
    addChore(parentId: string, request: CreateChoreRequest): Observable<Chore> {
        return this.http.post<Chore>(`${this.baseUrl}/${parentId}/chores`, request);
    }

    // Get all chores for a parent
    getChores(parentId: string): Observable<Chore[]> {
        return this.http.get<Chore[]>(`${this.baseUrl}/${parentId}/chores`);
    }

    // Add a store item
    addStoreItem(parentId: string, request: CreateStoreItemRequest): Observable<StoreItem> {
        return this.http.post<StoreItem>(`${this.baseUrl}/${parentId}/store-items`, request);
    }

    // Pay child for completing a chore
    payChildForChore(parentId: string, request: PayChildRequest): Observable<Transaction> {
        return this.http.post<Transaction>(`${this.baseUrl}/${parentId}/pay-child`, request);
    }
}
