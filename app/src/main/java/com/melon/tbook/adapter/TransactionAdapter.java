package com.melon.tbook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.melon.tbook.R;
import com.melon.tbook.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactionList;
    private OnTransactionClickListener transactionClickListener;


    public interface OnTransactionClickListener{
        void onTransactionClick(Transaction transaction);
    }


    public void setTransactionClickListener(OnTransactionClickListener transactionClickListener) {
        this.transactionClickListener = transactionClickListener;
    }

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        String formattedAmount = String.format(Locale.getDefault(), "%.2f", transaction.getAmount());
        holder.textAmount.setText(formattedAmount);
        holder.textDescription.setText(transaction.getDescription());
        holder.textType.setText(transaction.getType());
        holder.textSubAccount.setText(transaction.getSubAccount());
        holder.textBorrower.setText(transaction.getBorrower());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        holder.textTime.setText(sdf.format(new Date()));
        holder.buttonDelete.setOnClickListener(v -> {
            if(transactionClickListener!=null){
                transactionClickListener.onTransactionClick(transaction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public void setTransactions(List<Transaction> transactions){
        this.transactionList = transactions;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textAmount;
        TextView textDescription;
        TextView textSubAccount;
        TextView textType;
        TextView textBorrower;
        TextView textTime;
        Button buttonDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textAmount = itemView.findViewById(R.id.text_amount);
            textDescription = itemView.findViewById(R.id.text_description);
            textSubAccount = itemView.findViewById(R.id.text_sub_account);
            textType = itemView.findViewById(R.id.text_type);
            textBorrower = itemView.findViewById(R.id.text_borrower);
            textTime = itemView.findViewById(R.id.text_time);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }
}