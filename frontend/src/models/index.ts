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
    timestamp: string;
}

export enum ChoreStatus {
    AVAILABLE = 'AVAILABLE',
    PENDING = 'PENDING',
    COMPLETED = 'COMPLETED'
}

export interface Chore {
    choreId: string;
    choreName: string;
    choreDescription: string;
    chorePrice: number;
    assignedChildId: string | null;
    available: boolean;
    status: ChoreStatus;
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

// Child with PIN (returned when parent creates a child)
export interface ChildWithPin extends Child {
    pin: string;
}

// Parent model matching Parent.java
export interface Parent {
    parentId: string;
    name: string;
    email: string;
    username: string;
    children: Child[];
    chores: Chore[];
    storeInventory: StoreItem[];
    transactions: Transaction[];
}

// Request/Response DTOs
export interface CreateParentRequest {
    name: string;
    email: string;
    username: string;
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

export interface PinResponse {
    pin: string;
}