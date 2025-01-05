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
        holder.textIncome.setText(String.valueOf(subAccountInfo.getTotalIncome()));
        holder.textExpense.setText(String.valueOf(subAccountInfo.getTotalExpense()));
        double balance = subAccountInfo.getTotalIncome() - subAccountInfo.getTotalExpense();
        holder.textBalance.setText(String.valueOf(balance));

        //  设置背景
        Drawable[] drawables = new Drawable[2];
        drawables[0] = holder.itemView.getContext().getResources().getDrawable(R.drawable.rounded_background);
        drawables[1] = holder.itemView.getContext().getResources().getDrawable(R.drawable.rounded_background1);
        holder.itemView.setBackground(drawables[position % drawables.length]);
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