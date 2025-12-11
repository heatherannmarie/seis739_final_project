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

    login(username: string, pin: string): Observable<Child> {
        return this.http.post<Child>(`${this.baseUrl}/login`, { username, pin });
    }

    getChild(childId: string): Observable<Child> {
        return this.http.get<Child>(`${this.baseUrl}/${childId}`);
    }

    getBalance(childId: string): Observable<BalanceResponse> {
        return this.http.get<BalanceResponse>(`${this.baseUrl}/${childId}/balance`);
    }

    getTransactions(childId: string): Observable<Transaction[]> {
        return this.http.get<Transaction[]>(`${this.baseUrl}/${childId}/transactions`);
    }

    getAvailableChores(childId: string): Observable<Chore[]> {
        return this.http.get<Chore[]>(`${this.baseUrl}/${childId}/available-chores`);
    }

    getStoreItems(childId: string): Observable<StoreItem[]> {
        return this.http.get<StoreItem[]>(`${this.baseUrl}/${childId}/store-items`);
    }

    requestChoreCompletion(childId: string, choreId: string): Observable<Chore> {
        return this.http.post<Chore>(`${this.baseUrl}/${childId}/chores/${choreId}/request-completion`, {});
    }

    purchaseItem(childId: string, itemId: string): Observable<Transaction> {
        return this.http.post<Transaction>(`${this.baseUrl}/${childId}/purchase/${itemId}`, {});
    }
}