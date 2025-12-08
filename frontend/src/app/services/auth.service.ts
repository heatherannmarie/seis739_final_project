// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private currentParentId: string | null = null;
    private currentChildId: string | null = null;

    setParent(parentId: string) {
        this.currentParentId = parentId;
        this.currentChildId = null;
    }

    setChild(childId: string) {
        this.currentChildId = childId;
        this.currentParentId = null;
    }

    getParentId(): string | null {
        return this.currentParentId;
    }

    getChildId(): string | null {
        return this.currentChildId;
    }

    isLoggedIn(): boolean {
        return this.currentParentId !== null || this.currentChildId !== null;
    }

    logout() {
        this.currentParentId = null;
        this.currentChildId = null;
    }
}