package com.melon.tbook.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.melon.tbook.model.Transaction;
import com.melon.tbook.model.TransactionType;
import com.melon.tbook.model.SubAccount;
import com.melon.tbook.model.SubAccountInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "transactions.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_SUB_ACCOUNT = "subAccount";
    private static final String COLUMN_BORROWER = "borrower";


    private static final String TABLE_TRANSACTION_TYPES = "transaction_types";
    private static final String COLUMN_TYPE_ID = "id";
    private static final String COLUMN_TYPE_NAME = "name";
    private static final String COLUMN_TYPE_TYPE = "type";

    private static final String TABLE_SUB_ACCOUNTS = "sub_accounts";
    private static final String COLUMN_SUB_ACCOUNT_ID = "id";
    private static final String COLUMN_SUB_ACCOUNT_NAME = "name";


    private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE " +
            TABLE_TRANSACTIONS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_AMOUNT + " REAL," +
            COLUMN_DESCRIPTION + " TEXT," +
            COLUMN_TYPE + " TEXT," +
            COLUMN_SUB_ACCOUNT + " TEXT," +
            COLUMN_BORROWER + " TEXT" +
            ")";
    private static final String CREATE_TABLE_TRANSACTION_TYPES = "CREATE TABLE " +
            TABLE_TRANSACTION_TYPES + "(" +
            COLUMN_TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_TYPE_NAME + " TEXT," +
            COLUMN_TYPE_TYPE + " TEXT" +
            ")";
    private static final String CREATE_TABLE_SUB_ACCOUNTS = "CREATE TABLE " +
            TABLE_SUB_ACCOUNTS + "(" +
            COLUMN_SUB_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_SUB_ACCOUNT_NAME + " TEXT" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRANSACTIONS);
        db.execSQL(CREATE_TABLE_TRANSACTION_TYPES);
        db.execSQL(CREATE_TABLE_SUB_ACCOUNTS);
        // 添加默认的交易类型
        addDefaultTransactionTypes(db);
    }
    private void addDefaultTransactionTypes(SQLiteDatabase db) {
        List<String> defaultIncomeTypes = Arrays.asList(
                "工资薪水", "副业收入", "投资收益", "红包礼金", "奖金", "租金收入", "其他收入"
        );
        List<String> defaultExpenseTypes = Arrays.asList(
                "生活消费", "住房支出", "交通支出", "通讯支出", "保险", "娱乐休闲", "教育支出", "医疗支出", "家庭支出", "储蓄投资", "其他支出"
        );
        for(String type : defaultIncomeTypes){
            addTransactionType(db, new TransactionType(type,"收入"));
        }
        for(String type: defaultExpenseTypes){
            addTransactionType(db, new TransactionType(type,"支出"));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION_TYPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUB_ACCOUNTS);
        onCreate(db);
        /*
        if(oldVersion < 2){
            // 如果是旧版本，则添加 subAccount 和 borrower
            db.execSQL("ALTER TABLE " + TABLE_TRANSACTIONS + " ADD COLUMN " + COLUMN_SUB_ACCOUNT + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_TRANSACTIONS + " ADD COLUMN " + COLUMN_BORROWER + " TEXT");
        }else if (oldVersion < 3){
            db.execSQL(CREATE_TABLE_SUB_ACCOUNTS);
        }else{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION_TYPES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUB_ACCOUNTS);
            onCreate(db);
        }
        */
    }

    public long addTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        values.put(COLUMN_DESCRIPTION, transaction.getDescription());
        values.put(COLUMN_TYPE, transaction.getType());
        values.put(COLUMN_SUB_ACCOUNT, transaction.getSubAccount());
        values.put(COLUMN_BORROWER, transaction.getBorrower());
        long id = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
        return id;
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRANSACTIONS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE));
                String subAccount = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUB_ACCOUNT));
                String borrower = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BORROWER));
                Transaction transaction = new Transaction(id, amount, description, type,subAccount,borrower);
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transactionList;
    }

    public void deleteTransaction(int transactionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(transactionId)});
        db.close();
    }
    public long addTransactionType(SQLiteDatabase db,TransactionType transactionType) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE_NAME, transactionType.getName());
        values.put(COLUMN_TYPE_TYPE, transactionType.getType());
        long id = db.insert(TABLE_TRANSACTION_TYPES, null, values);
        return id;
    }
    public long addTransactionType(TransactionType transactionType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE_NAME, transactionType.getName());
        values.put(COLUMN_TYPE_TYPE, transactionType.getType());
        long id = db.insert(TABLE_TRANSACTION_TYPES, null, values);
        db.close();
        return id;
    }
    public List<TransactionType> getAllTransactionTypes(String type) {
        List<TransactionType> transactionTypeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRANSACTION_TYPES, null, COLUMN_TYPE_TYPE +" = ?", new String[]{type}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TYPE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE_NAME));
                String transactionType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE_TYPE));
                TransactionType transactionType1 = new TransactionType(id, name, transactionType);
                transactionTypeList.add(transactionType1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionTypeList;
    }
    public void deleteTransactionType(int transactionTypeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTION_TYPES, COLUMN_TYPE_ID + " = ?", new String[]{String.valueOf(transactionTypeId)});
        db.close();
    }
    public long addSubAccount(SubAccount subAccount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUB_ACCOUNT_NAME, subAccount.getName());
        long id = db.insert(TABLE_SUB_ACCOUNTS, null, values);
        db.close();
        return id;
    }
    public List<SubAccount> getAllSubAccounts() {
        List<SubAccount> subAccountList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SUB_ACCOUNTS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUB_ACCOUNT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUB_ACCOUNT_NAME));
                SubAccount subAccount = new SubAccount(id, name);
                subAccountList.add(subAccount);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return subAccountList;
    }
    public void deleteSubAccount(int subAccountId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUB_ACCOUNTS, COLUMN_SUB_ACCOUNT_ID + " = ?", new String[]{String.valueOf(subAccountId)});
        db.close();
    }

    public List<SubAccountInfo> getSubAccountInfoList() {
        List<SubAccountInfo> subAccountInfoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+ COLUMN_SUB_ACCOUNT + ", SUM(CASE WHEN " + COLUMN_TYPE + " = '收入' THEN " + COLUMN_AMOUNT + " ELSE 0 END) AS total_income, SUM(CASE WHEN " + COLUMN_TYPE + " = '支出' THEN " + COLUMN_AMOUNT + " ELSE 0 END) AS total_expense FROM "+TABLE_TRANSACTIONS +  " GROUP BY "+COLUMN_SUB_ACCOUNT;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                String subAccountName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUB_ACCOUNT));
                double totalIncome = cursor.getDouble(cursor.getColumnIndexOrThrow("total_income"));
                double totalExpense = cursor.getDouble(cursor.getColumnIndexOrThrow("total_expense"));
                SubAccountInfo info = new SubAccountInfo(subAccountName,totalIncome,totalExpense);
                subAccountInfoList.add(info);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return subAccountInfoList;
    }

    public double getTotalTransactionAmount(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{type});
        double totalAmount = 0;
        if (cursor.moveToFirst()) {
            totalAmount = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return totalAmount;
    }
}