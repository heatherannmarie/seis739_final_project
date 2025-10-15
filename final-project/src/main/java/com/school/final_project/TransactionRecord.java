package com.school.final_project;

import java.util.ArrayList;

public class TransactionRecord {
    ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
    float balanceTotal;

    ArrayList<Transaction> getAll(User accountUser) {
        return transactionList;
    }
}
