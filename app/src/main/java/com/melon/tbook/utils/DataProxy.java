package com.melon.tbook.utils;

import android.content.Context;

import com.melon.tbook.model.SubAccount;
import com.melon.tbook.model.Transaction;
import com.melon.tbook.model.TransactionType;
import com.melon.tbook.model.SubAccountInfo;


import java.util.List;

public abstract class DataProxy {
    private static DataProxy instance;


    public static synchronized DataProxy getInstance(Context context) {
        if (instance == null) {
            instance = new DBDataProxy(context);
        }
        return instance;
    }
    public abstract long addTransaction(Transaction transaction);
    public abstract List<Transaction> getAllTransactions();
    public abstract void deleteTransaction(int transactionId);
    public abstract long addTransactionType(TransactionType transactionType) ;
    public abstract List<TransactionType> getAllTransactionTypes(String type) ;
    public abstract void deleteTransactionType(int transactionTypeId) ;
    public abstract long addSubAccount(SubAccount subAccount);
    public abstract List<SubAccountInfo> getSubAccountInfoList() ;
    public abstract double getTotalTransactionAmount(String type);
    public abstract void destroy();
}