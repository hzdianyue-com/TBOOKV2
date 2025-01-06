package com.melon.tbook.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.melon.tbook.R;
import com.melon.tbook.adapter.ManageTransactionTypeAdapter;
import com.melon.tbook.model.Transaction;
import com.melon.tbook.model.TransactionType;
import com.melon.tbook.model.SubAccount;
import com.melon.tbook.utils.DataProxy;
import com.melon.tbook.utils.DecimalDigitsInputFilter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TransactionFragment extends Fragment  implements ManageTransactionTypeAdapter.OnTransactionTypeClickListener {
    private DataProxy dbHelper;
    private Spinner spinnerTransactionType;
    private Spinner spinnerSubAccount;
    private ImageView buttonManageSubAccount;
    private EditText editAmount;
    private Spinner spinnerType;
    private ImageView buttonManageType;
    private EditText editBorrower;
    private EditText editDescription;
    private EditText textTime;
    private Button buttonAddTransaction;
    private SharedPreferences sharedPreferences;
    private List<String> subAccounts;
    private List<TransactionType> transactionTypes;
    private Calendar calendar;
    private  boolean isFirstRun;
    private  ManageTransactionTypeAdapter manageTransactionTypeAdapter;


    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        spinnerTransactionType = view.findViewById(R.id.spinner_transaction_type);
        spinnerSubAccount = view.findViewById(R.id.spinner_sub_account);
        buttonManageSubAccount = view.findViewById(R.id.button_manage_sub_account);
        editAmount = view.findViewById(R.id.edit_amount);
        spinnerType = view.findViewById(R.id.spinner_type);
        buttonManageType = view.findViewById(R.id.button_manage_type);
        editBorrower = view.findViewById(R.id.edit_borrower);
        editDescription = view.findViewById(R.id.edit_description);
        textTime = view.findViewById(R.id.text_time);
        buttonAddTransaction = view.findViewById(R.id.button_add_transaction);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = DataProxy.getInstance(getContext());
        sharedPreferences = getContext().getSharedPreferences("tbook_prefs", Context.MODE_PRIVATE);
        // 初始化时间
        calendar = Calendar.getInstance();
        updateTimeText();
        textTime.setOnClickListener(v -> showDateTimePicker());
        // 初始化子账户
        subAccounts = getSubAccountsFromPreferences();
        ArrayAdapter<String> subAccountAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, subAccounts);
        subAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubAccount.setAdapter(subAccountAdapter);
        buttonManageSubAccount.setOnClickListener(v -> showManageSubAccountDialog());
        // 初始化类型
        transactionTypes = getTransactionTypesFromPreferences("收入");
        ArrayAdapter<String> transactionTypeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getTransactionTypeNames(transactionTypes));
        transactionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(transactionTypeAdapter);
        buttonManageType.setOnClickListener(v -> showManageTransactionTypeDialog());
        // 初始化 Spinner
        String[] transactionTypesSpinner = {getString(R.string.text_income), getString(R.string.text_expense), getString(R.string.text_lend), getString(R.string.text_borrow)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, transactionTypesSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTransactionType.setAdapter(adapter);
        spinnerTransactionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();
                setTransactionTypesBySelect(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 默认选中第一个
        spinnerTransactionType.setSelection(0);
        setTransactionTypesBySelect(spinnerTransactionType.getSelectedItem().toString());

        editAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

        buttonAddTransaction.setOnClickListener(v -> {
            addTransaction();
        });
    }
    private void showDateTimePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateTimeText();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void showTimePicker(){
        // 不需要时间选择器
    }
    private void updateTimeText(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedTime = sdf.format(calendar.getTime());
        textTime.setText(formattedTime);
    }
    private void addTransaction() {
        String type =  null;
        if(spinnerTransactionType.getSelectedItem() != null){
            type = spinnerTransactionType.getSelectedItem().toString();
        }
        String subAccount = spinnerSubAccount.getSelectedItem().toString();
        String amountText = editAmount.getText().toString();
        String borrower = editBorrower.getText().toString();
        String description = editDescription.getText().toString();

        if (amountText.isEmpty()) {
            Toast.makeText(getContext(), R.string.message_input_amount_and_description, Toast.LENGTH_SHORT).show();
            return;
        }
        double amount = Double.parseDouble(amountText);
        String transactionType;
        if(type!= null && type.equals(getString(R.string.text_income))){
            transactionType = "收入";
        }else if(type!= null && type.equals(getString(R.string.text_expense))){
            transactionType = "支出";
        }else if(type!= null && type.equals(getString(R.string.text_borrow))){
            transactionType = "借入";
        }else{
            transactionType = "借出";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = sdf.format(calendar.getTime());
        Transaction transaction = new Transaction(amount, description, transactionType, subAccount, borrower);
        dbHelper.addTransaction(transaction);
        Toast.makeText(getContext(), R.string.message_add_transaction_success, Toast.LENGTH_SHORT).show();
    }

    private void showManageSubAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.title_manage_sub_account);
        final String[] subAccountArray = subAccounts.toArray(new String[0]);
        List<String> defaultAccounts = Arrays.asList(getString(R.string.default_bank_account), getString(R.string.default_online_account), getString(R.string.default_alipay), getString(R.string.default_wechat_wallet), getString(R.string.default_financial_account));
        builder.setItems(subAccountArray,(dialog,which) ->{
            String selectedAccount = subAccountArray[which];
            if(defaultAccounts.contains(selectedAccount)){
                Toast.makeText(getContext(), R.string.message_default_account_can_not_delete, Toast.LENGTH_SHORT).show();
            }else{
                showRemoveSubAccountConfirmDialog(selectedAccount);
            }
        });
        builder.setNeutralButton(R.string.button_add, (dialog, which) ->{
            showAddSubAccountDialog();
        });
        builder.setNegativeButton(R.string.button_cancel, null);
        builder.show();
    }
    private void showRemoveSubAccountConfirmDialog(String account){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.title_confirm_delete);
        builder.setMessage(getString(R.string.message_confirm_delete_sub_account,account));
        builder.setPositiveButton(R.string.button_delete,(dialog,which) ->{
            subAccounts.remove(account);
            saveSubAccountsToPreferences(subAccounts);
            ArrayAdapter<String> subAccountAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, subAccounts);
            subAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSubAccount.setAdapter(subAccountAdapter);
        });
        builder.setNegativeButton(R.string.button_cancel, null);
        builder.show();
    }
    private void showAddSubAccountDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.title_add_sub_account);
        final EditText input = new EditText(getContext());
        builder.setView(input);
        builder.setPositiveButton(R.string.button_add, (dialog, which) ->{
            String subAccount = input.getText().toString();
            if(!subAccount.isEmpty()){
                SubAccount newSubAccount = new SubAccount(subAccount);
                dbHelper.addSubAccount(newSubAccount);
                subAccounts.add(subAccount);
                saveSubAccountsToPreferences(subAccounts);
                ArrayAdapter<String> subAccountAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, subAccounts);
                subAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSubAccount.setAdapter(subAccountAdapter);
            }
        });
        builder.setNegativeButton(R.string.button_cancel, null);
        builder.show();
    }
    private void showManageTransactionTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.title_manage_type);
        final List<TransactionType> typesArray = getTransactionTypesFromPreferences();
        List<TransactionType> defaultIncomeTypes = dbHelper.getAllTransactionTypes("收入");
        List<TransactionType> defaultExpenseTypes = dbHelper.getAllTransactionTypes("支出");
        manageTransactionTypeAdapter = new ManageTransactionTypeAdapter(typesArray);
        manageTransactionTypeAdapter.setTransactionTypeClickListener(this);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_manage_transaction_type,null);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_manage_type);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(manageTransactionTypeAdapter);
        EditText input = view.findViewById(R.id.edit_add_type);
        builder.setView(view);

        builder.setNeutralButton(R.string.button_add, (dialog, which) ->{
            String newType = input.getText().toString();
            if(!newType.isEmpty()){
                String selectedTypeText = spinnerTransactionType.getSelectedItem().toString();
                TransactionType transactionType = new TransactionType(newType,selectedTypeText);
                dbHelper.addTransactionType(transactionType);
                transactionTypes = getTransactionTypesFromPreferences();
                manageTransactionTypeAdapter.setTransactionTypes(transactionTypes);
                setTransactionTypesBySelect(spinnerTransactionType.getSelectedItem().toString());
            }
        });
        builder.setNegativeButton(R.string.button_cancel, (dialog, which) ->{
            setTransactionTypesBySelect(spinnerTransactionType.getSelectedItem().toString());
        });
        builder.show();
    }
    private void showRemoveTransactionTypeConfirmDialog(TransactionType transactionType){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.title_confirm_delete);
        builder.setMessage(getString(R.string.message_confirm_delete_type,transactionType.getName()));
        builder.setPositiveButton(R.string.button_delete,(dialog,which) ->{
            String selectedType = spinnerTransactionType.getSelectedItem().toString();
            List<TransactionType> defaultIncomeTypes = dbHelper.getAllTransactionTypes("收入");
            List<TransactionType> defaultExpenseTypes = dbHelper.getAllTransactionTypes("支出");
            if (selectedType.equals(getString(R.string.text_income)) && defaultIncomeTypes.contains(transactionType.getName()) || selectedType.equals(getString(R.string.text_expense)) && defaultExpenseTypes.contains(transactionType.getName())){
                Toast.makeText(getContext(), "默认类型不能删除", Toast.LENGTH_SHORT).show();
            }else{
                dbHelper.deleteTransactionType(transactionType.getId());
                transactionTypes = getTransactionTypesFromPreferences();
                manageTransactionTypeAdapter.setTransactionTypes(transactionTypes);
            }

        });
        builder.setNegativeButton(R.string.button_cancel, null);
        builder.show();
    }
    private void saveSubAccountsToPreferences(List<String> subAccounts) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder sb = new StringBuilder();
        for (String account : subAccounts) {
            sb.append(account).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        editor.putString("sub_accounts", sb.toString());
        editor.putBoolean("first_run",false);
        editor.apply();
    }

    private List<String> getSubAccountsFromPreferences() {
        String subAccountString = sharedPreferences.getString("sub_accounts", "");
        List<String> accounts = new ArrayList<>();
        isFirstRun = sharedPreferences.getBoolean("first_run",true);
        if (subAccountString.isEmpty() && isFirstRun) {
            // 默认的子账户
            accounts.addAll(Arrays.asList(
                    getString(R.string.default_bank_account), getString(R.string.default_online_account), getString(R.string.default_alipay), getString(R.string.default_wechat_wallet), getString(R.string.default_financial_account)
            ));
            saveSubAccountsToPreferences(accounts);
        }else if(!subAccountString.isEmpty()){
            String[] subAccountArray = subAccountString.split(",");
            for(String subAccount : subAccountArray){
                accounts.add(subAccount);
            }
        }
        return accounts;
    }
    private void saveTransactionTypesToPreferences(List<TransactionType> transactionTypes, String type) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder sb = new StringBuilder();
        for (TransactionType item : transactionTypes) {
            sb.append(item.getName()).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        editor.putString("transaction_types_" + type, sb.toString());
        editor.apply();
    }
    private List<TransactionType> getTransactionTypesFromPreferences() {
        String type =  null;
        if(spinnerTransactionType.getSelectedItem() != null){
            type = spinnerTransactionType.getSelectedItem().toString();
        }
        String key;
        if(type!= null && type.equals(getString(R.string.text_income))){
            key = "收入";
        }else{
            key = "支出";
        }
        List<TransactionType> types = dbHelper.getAllTransactionTypes(key);
        isFirstRun = sharedPreferences.getBoolean("first_run",true);
        if (types.isEmpty() && isFirstRun) {
            types.addAll(dbHelper.getAllTransactionTypes(type));
            saveTransactionTypesToPreferences(types,type);
        }
        return types;
    }
    private List<TransactionType> getTransactionTypesFromPreferences(String type) {
        String key;
        if(type!= null && type.equals(getString(R.string.text_income))){
            key = "收入";
        }else{
            key = "支出";
        }
        List<TransactionType> types = dbHelper.getAllTransactionTypes(key);
        isFirstRun = sharedPreferences.getBoolean("first_run",true);
        if (types.isEmpty() && isFirstRun) {
            types.addAll(dbHelper.getAllTransactionTypes(type));
            saveTransactionTypesToPreferences(types,type);
        }
        return types;
    }
    private List<String> getTransactionTypeNames(List<TransactionType> transactionTypeList){
        List<String> types = new ArrayList<>();
        for(TransactionType transactionType : transactionTypeList){
            types.add(transactionType.getName());
        }
        return types;
    }

    private void setTransactionTypesBySelect(String selectedType){
        List<TransactionType> transactionTypeList = dbHelper.getAllTransactionTypes(selectedType);
        ArrayAdapter<String> transactionTypeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                getTransactionTypeNames(transactionTypeList));
        transactionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(transactionTypeAdapter);
        transactionTypes = getTransactionTypesFromPreferences();
    }

    @Override
    public void onTransactionTypeClick(TransactionType transactionType) {
        showRemoveTransactionTypeConfirmDialog(transactionType);
    }
}