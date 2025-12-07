// Transaction types matching Java enum
export enum TransactionType {
    ALLOWANCE = 'ALLOWANCE',
    PURCHASE = 'PURCHASE'
}

// Transaction model matching Transaction.java
export interface Transaction {
    id?: number;
    type: TransactionType;
    amount: number;
    childId: string;
    description: string;
    timestamp: string; // ISO date string
}

// Chore model matching Chore.java
export interface Chore {
    choreId: string;
    choreName: string;
    choreDescription: string;
    chorePrice: number;
    assignedChildId: string | null;
    available: boolean;
}

// StoreItem model matching StoreItem.java
export interface StoreItem {
    itemID: string;
    itemName: string;
    availableInventory: number;
    itemPrice: number;
    available: boolean;
}

// Child model matching Child.java
export interface Child {
    childId: string;
    name: string;
    username: string;
    balance: number;
    parentId: string;
    transactionHistory: Transaction[];
}

// Parent model matching Parent.java
export interface Parent {
    parentId: string;
    name: string;
    email?: string;
    username?: string;
    children: Child[];
    chores: Chore[];
    storeInventory: StoreItem[];
    transactions: Transaction[];
}

// Request/Response DTOs
export interface CreateParentRequest {
    name: string;
}

export interface CreateChildRequest {
    name: string;
    username: string;
}

export interface CreateChoreRequest {
    choreName: string;
    choreDescription: string;
    chorePrice: number;
    assignedChildId?: string;
}

export interface CreateStoreItemRequest {
    itemName: string;
    availableInventory: number;
    itemPrice: number;
}

export interface PayChildRequest {
    childId: string;
    choreId: string;
}

export interface BalanceResponse {
    balance: number;
}
