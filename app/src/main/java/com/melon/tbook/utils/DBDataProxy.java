package com.melon.tbook.utils;

import android.content.Context;

import com.melon.tbook.model.SubAccount;
import com.melon.tbook.model.Transaction;
import com.melon.tbook.model.TransactionType;
import com.melon.tbook.model.SubAccountInfo;
import com.melon.tbook.model.User;

import java.util.List;

public class DBDataProxy extends DataProxy {
    private DatabaseHelper dbHelper;
    public DBDataProxy(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    @Override
    public long addTransaction(Transaction transaction) {
        return dbHelper.addTransaction(transaction);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return dbHelper.getAllTransactions();
    }

    @Override
    public void deleteTransaction(int transactionId) {
        dbHelper.deleteTransaction(transactionId);
    }

    @Override
    public long addTransactionType(TransactionType transactionType) {
        return dbHelper.addTransactionType(transactionType);
    }

    @Override
    public List<TransactionType> getAllTransactionTypes(String type) {
        return dbHelper.getAllTransactionTypes(type);
    }

    @Override
    public void deleteTransactionType(int transactionTypeId) {
        dbHelper.deleteTransactionType(transactionTypeId);
    }

    @Override
    public long addSubAccount(SubAccount subAccount) {
        return dbHelper.addSubAccount(subAccount);
    }

    @Override
    public List<SubAccountInfo> getSubAccountInfoList() {
        return dbHelper.getSubAccountInfoList();
    }

    @Override
    public double getTotalTransactionAmount(String type) {
        return dbHelper.getTotalTransactionAmount(type);
    }

    @Override
    public long addUser(User user) {
        return dbHelper.addUser(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return dbHelper.getUserByUsername(username);
    }

    @Override
    public void updateUser(User user) {
        dbHelper.updateUser(user);
    }

    @Override
    public void deleteUser(int userId) {
        dbHelper.deleteUser(userId);
    }

    @Override
    public void destroy() {
        dbHelper.close();
    }

}