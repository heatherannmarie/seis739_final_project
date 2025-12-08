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
}
