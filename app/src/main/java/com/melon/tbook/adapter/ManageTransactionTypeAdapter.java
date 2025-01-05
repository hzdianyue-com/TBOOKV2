package com.melon.tbook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.melon.tbook.R;
import com.melon.tbook.model.TransactionType;

import java.util.List;


public class ManageTransactionTypeAdapter extends RecyclerView.Adapter<ManageTransactionTypeAdapter.ViewHolder> {
    private List<TransactionType> transactionTypeList;
    private OnTransactionTypeClickListener transactionTypeClickListener;

    public ManageTransactionTypeAdapter(List<TransactionType> transactionTypeList){
        this.transactionTypeList = transactionTypeList;
    }
    public void setTransactionTypeClickListener(OnTransactionTypeClickListener transactionTypeClickListener) {
        this.transactionTypeClickListener = transactionTypeClickListener;
    }

    public interface OnTransactionTypeClickListener{
        void onTransactionTypeClick(TransactionType transactionType);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_manage_transaction_type, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionType transactionType = transactionTypeList.get(position);
        holder.textTypeName.setText(transactionType.getName());
        holder.buttonDeleteType.setOnClickListener(v ->{
            if(transactionTypeClickListener != null){
                transactionTypeClickListener.onTransactionTypeClick(transactionType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionTypeList.size();
    }

    public void setTransactionTypes(List<TransactionType> transactionTypes){
        this.transactionTypeList = transactionTypes;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textTypeName;
        Button buttonDeleteType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTypeName = itemView.findViewById(R.id.text_type_name);
            buttonDeleteType = itemView.findViewById(R.id.button_delete_type);
        }
    }
}