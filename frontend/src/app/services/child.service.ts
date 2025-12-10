import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Child, Chore, StoreItem, Transaction, BalanceResponse } from '../../models';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class ChildService {
    private http = inject(HttpClient);
    private baseUrl = `${environment.apiUrl}/children`;

    // Login with username and PIN
    login(username: string, pin: string): Observable<Child> {
        return this.http.post<Child>(`${this.baseUrl}/login`, { username, pin });
    }

    // Get child by ID
    getChild(childId: string): Observable<Child> {
        return this.http.get<Child>(`${this.baseUrl}/${childId}`);
    }

    // Get child's current balance
    getBalance(childId: string): Observable<BalanceResponse> {
        return this.http.get<BalanceResponse>(`${this.baseUrl}/${childId}/balance`);
    }

    // Get child's transaction history
    getTransactions(childId: string): Observable<Transaction[]> {
        return this.http.get<Transaction[]>(`${this.baseUrl}/${childId}/transactions`);
    }

    // Get available chores for the child
    getAvailableChores(childId: string): Observable<Chore[]> {
        return this.http.get<Chore[]>(`${this.baseUrl}/${childId}/available-chores`);
    }

    // Get store items available for purchase
    getStoreItems(childId: string): Observable<StoreItem[]> {
        return this.http.get<StoreItem[]>(`${this.baseUrl}/${childId}/store-items`);
    }

    // Request chore completion (mark as pending)
    requestChoreCompletion(childId: string, choreId: string): Observable<Chore> {
        return this.http.post<Chore>(`${this.baseUrl}/${childId}/chores/${choreId}/request-completion`, {});
    }

    // Purchase an item from the store
    purchaseItem(childId: string, itemId: string): Observable<Transaction> {
        return this.http.post<Transaction>(`${this.baseUrl}/${childId}/purchase/${itemId}`, {});
    }
}