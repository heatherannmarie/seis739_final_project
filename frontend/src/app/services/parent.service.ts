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

    createParent(request: CreateParentRequest): Observable<Parent> {
        return this.http.post<Parent>(this.baseUrl, request);
    }

    login(username: string): Observable<Parent> {
        return this.http.post<Parent>(`${this.baseUrl}/login`, { username });
    }

    getParent(parentId: string): Observable<Parent> {
        return this.http.get<Parent>(`${this.baseUrl}/${parentId}`);
    }

    createChild(parentId: string, request: CreateChildRequest): Observable<ChildWithPin> {
        return this.http.post<ChildWithPin>(`${this.baseUrl}/${parentId}/children`, request);
    }

    getChildren(parentId: string): Observable<Child[]> {
        return this.http.get<Child[]>(`${this.baseUrl}/${parentId}/children`);
    }

    getChildPin(parentId: string, childId: string): Observable<PinResponse> {
        return this.http.get<PinResponse>(`${this.baseUrl}/${parentId}/children/${childId}/pin`);
    }

    regenerateChildPin(parentId: string, childId: string): Observable<PinResponse> {
        return this.http.post<PinResponse>(`${this.baseUrl}/${parentId}/children/${childId}/regenerate-pin`, {});
    }

    updateChild(parentId: string, childId: string, request: Partial<CreateChildRequest>): Observable<Child> {
        return this.http.put<Child>(`${this.baseUrl}/${parentId}/children/${childId}`, request);
    }

    addChore(parentId: string, request: CreateChoreRequest): Observable<Chore> {
        return this.http.post<Chore>(`${this.baseUrl}/${parentId}/chores`, request);
    }

    getChores(parentId: string): Observable<Chore[]> {
        return this.http.get<Chore[]>(`${this.baseUrl}/${parentId}/chores`);
    }

    updateChore(parentId: string, choreId: string, request: Partial<CreateChoreRequest>): Observable<Chore> {
        return this.http.put<Chore>(`${this.baseUrl}/${parentId}/chores/${choreId}`, request);
    }

    deleteChore(parentId: string, choreId: string): Observable<{ message: string }> {
        return this.http.delete<{ message: string }>(`${this.baseUrl}/${parentId}/chores/${choreId}`);
    }

    getPendingChores(parentId: string): Observable<Chore[]> {
        return this.http.get<Chore[]>(`${this.baseUrl}/${parentId}/chores/pending`);
    }

    approveChore(parentId: string, choreId: string): Observable<Transaction> {
        return this.http.post<Transaction>(`${this.baseUrl}/${parentId}/chores/${choreId}/approve`, {});
    }

    denyChore(parentId: string, choreId: string): Observable<Chore> {
        return this.http.post<Chore>(`${this.baseUrl}/${parentId}/chores/${choreId}/deny`, {});
    }

    addStoreItem(parentId: string, request: CreateStoreItemRequest): Observable<StoreItem> {
        return this.http.post<StoreItem>(`${this.baseUrl}/${parentId}/store-items`, request);
    }

    getStoreItems(parentId: string): Observable<StoreItem[]> {
        return this.http.get<StoreItem[]>(`${this.baseUrl}/${parentId}/store-items`);
    }

    updateStoreItem(parentId: string, itemId: string, request: Partial<CreateStoreItemRequest>): Observable<StoreItem> {
        return this.http.put<StoreItem>(`${this.baseUrl}/${parentId}/store-items/${itemId}`, request);
    }

    deleteStoreItem(parentId: string, itemId: string): Observable<{ message: string }> {
        return this.http.delete<{ message: string }>(`${this.baseUrl}/${parentId}/store-items/${itemId}`);
    }

    getTransactions(parentId: string): Observable<Transaction[]> {
        return this.http.get<Transaction[]>(`${this.baseUrl}/${parentId}/transactions`);
    }

    payChildForChore(parentId: string, request: PayChildRequest): Observable<Transaction> {
        return this.http.post<Transaction>(`${this.baseUrl}/${parentId}/pay-child`, request);
    }

    addAllowance(parentId: string, childId: string, amount: number): Observable<Child> {
        return this.http.post<Child>(`${this.baseUrl}/${parentId}/children/${childId}/add-allowance`, { amount });
    }
}