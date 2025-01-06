package com.melon.tbook.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.melon.tbook.R;
import com.melon.tbook.model.SubAccountInfo;

import java.util.List;
import java.util.Locale;

public class SubAccountAdapter extends RecyclerView.Adapter<SubAccountAdapter.ViewHolder> {

    private List<SubAccountInfo> subAccountInfoList;

    public SubAccountAdapter(List<SubAccountInfo> subAccountInfoList) {
        this.subAccountInfoList = subAccountInfoList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_sub_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubAccountInfo subAccountInfo = subAccountInfoList.get(position);
        holder.textSubAccountName.setText(subAccountInfo.getSubAccountName());
        String formattedTotalIncome = String.format(Locale.getDefault(), "%.2f", subAccountInfo.getTotalIncome());
        holder.textIncome.setText(formattedTotalIncome);
        String formattedTotalExpense = String.format(Locale.getDefault(), "%.2f", subAccountInfo.getTotalExpense());
        holder.textExpense.setText(formattedTotalExpense);
        double balance = subAccountInfo.getTotalIncome() - subAccountInfo.getTotalExpense();
        String formattedbalance = String.format(Locale.getDefault(), "%.2f", balance);
        holder.textBalance.setText(formattedbalance);
    }

    @Override
    public int getItemCount() {
        return subAccountInfoList.size();
    }
    public void setSubAccounts(List<SubAccountInfo> subAccounts){
        this.subAccountInfoList = subAccounts;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textSubAccountName;
        TextView textIncome;
        TextView textExpense;
        TextView textBalance;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textSubAccountName = itemView.findViewById(R.id.text_sub_account_name);
            textIncome = itemView.findViewById(R.id.text_sub_account_income);
            textExpense = itemView.findViewById(R.id.text_sub_account_expense);
            textBalance = itemView.findViewById(R.id.text_sub_account_balance);
        }
    }
}