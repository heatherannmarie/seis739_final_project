import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
    Parent,
    Child,
    ChildWithPin,
    Chore,
    StoreItem,
    Transaction,
    CreateParentRequest,
    CreateChildRequest,
    CreateChoreRequest,
    CreateStoreItemRequest,
    PayChildRequest,
    PinResponse
} from '../../models';
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

    // Login with username
    login(username: string): Observable<Parent> {
        return this.http.post<Parent>(`${this.baseUrl}/login`, { username });
    }

    // Get parent by ID
    getParent(parentId: string): Observable<Parent> {
        return this.http.get<Parent>(`${this.baseUrl}/${parentId}`);
    }

    // Create a child account under a parent (returns child with PIN)
    createChild(parentId: string, request: CreateChildRequest): Observable<ChildWithPin> {
        return this.http.post<ChildWithPin>(`${this.baseUrl}/${parentId}/children`, request);
    }

    // Get all children for a parent
    getChildren(parentId: string): Observable<Child[]> {
        return this.http.get<Child[]>(`${this.baseUrl}/${parentId}/children`);
    }

    // Get a child's PIN
    getChildPin(parentId: string, childId: string): Observable<PinResponse> {
        return this.http.get<PinResponse>(`${this.baseUrl}/${parentId}/children/${childId}/pin`);
    }

    // Regenerate a child's PIN
    regenerateChildPin(parentId: string, childId: string): Observable<PinResponse> {
        return this.http.post<PinResponse>(`${this.baseUrl}/${parentId}/children/${childId}/regenerate-pin`, {});
    }

    // Update a child account
    updateChild(parentId: string, childId: string, request: Partial<CreateChildRequest>): Observable<Child> {
        return this.http.put<Child>(`${this.baseUrl}/${parentId}/children/${childId}`, request);
    }

    // Add a new chore
    addChore(parentId: string, request: CreateChoreRequest): Observable<Chore> {
        return this.http.post<Chore>(`${this.baseUrl}/${parentId}/chores`, request);
    }

    // Get all chores for a parent
    getChores(parentId: string): Observable<Chore[]> {
        return this.http.get<Chore[]>(`${this.baseUrl}/${parentId}/chores`);
    }

    // Update a chore
    updateChore(parentId: string, choreId: string, request: Partial<CreateChoreRequest>): Observable<Chore> {
        return this.http.put<Chore>(`${this.baseUrl}/${parentId}/chores/${choreId}`, request);
    }

    // Delete a chore
    deleteChore(parentId: string, choreId: string): Observable<{ message: string }> {
        return this.http.delete<{ message: string }>(`${this.baseUrl}/${parentId}/chores/${choreId}`);
    }

    // Get pending chores
    getPendingChores(parentId: string): Observable<Chore[]> {
        return this.http.get<Chore[]>(`${this.baseUrl}/${parentId}/chores/pending`);
    }

    // Approve chore completion
    approveChore(parentId: string, choreId: string): Observable<Transaction> {
        return this.http.post<Transaction>(`${this.baseUrl}/${parentId}/chores/${choreId}/approve`, {});
    }

    // Deny chore completion
    denyChore(parentId: string, choreId: string): Observable<Chore> {
        return this.http.post<Chore>(`${this.baseUrl}/${parentId}/chores/${choreId}/deny`, {});
    }

    // Add a store item
    addStoreItem(parentId: string, request: CreateStoreItemRequest): Observable<StoreItem> {
        return this.http.post<StoreItem>(`${this.baseUrl}/${parentId}/store-items`, request);
    }

    // Get all store items
    getStoreItems(parentId: string): Observable<StoreItem[]> {
        return this.http.get<StoreItem[]>(`${this.baseUrl}/${parentId}/store-items`);
    }

    // Update a store item
    updateStoreItem(parentId: string, itemId: string, request: Partial<CreateStoreItemRequest>): Observable<StoreItem> {
        return this.http.put<StoreItem>(`${this.baseUrl}/${parentId}/store-items/${itemId}`, request);
    }

    // Delete a store item
    deleteStoreItem(parentId: string, itemId: string): Observable<{ message: string }> {
        return this.http.delete<{ message: string }>(`${this.baseUrl}/${parentId}/store-items/${itemId}`);
    }

    // Get all transactions
    getTransactions(parentId: string): Observable<Transaction[]> {
        return this.http.get<Transaction[]>(`${this.baseUrl}/${parentId}/transactions`);
    }

    // Pay child for completing a chore (manual payment)
    payChildForChore(parentId: string, request: PayChildRequest): Observable<Transaction> {
        return this.http.post<Transaction>(`${this.baseUrl}/${parentId}/pay-child`, request);
    }

    // Add allowance to a child
    addAllowance(parentId: string, childId: string, amount: number): Observable<Child> {
        return this.http.post<Child>(`${this.baseUrl}/${parentId}/children/${childId}/add-allowance`, { amount });
    }
}