package com.melon.tbook.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.melon.tbook.R;
import com.melon.tbook.adapter.SubAccountAdapter;
import com.melon.tbook.model.SubAccountInfo;
import com.melon.tbook.model.Transaction;
import com.melon.tbook.utils.DataProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private DataProxy dbHelper;
    private TextView totalBalanceTextView;
    private RecyclerView recyclerView;
    private SubAccountAdapter adapter;
    private TextView totalBorrowTextView;
    private TextView totalLendTextView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = DataProxy.getInstance(getContext());
        totalBalanceTextView = view.findViewById(R.id.text_total_balance);
        recyclerView = view.findViewById(R.id.recycler_view_sub_accounts);
        totalBorrowTextView = view.findViewById(R.id.text_total_borrow);
        totalLendTextView = view.findViewById(R.id.text_total_lend);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // 添加 ItemDecoration 设置 item 间距
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(16));
        adapter = new SubAccountAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        updateUI();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbHelper.destroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        if(getContext() == null){
            return;
        }
        double totalIncome =  dbHelper.getTotalTransactionAmount("收入");
        double totalExpense = dbHelper.getTotalTransactionAmount("支出");
        double totalBorrow =  dbHelper.getTotalTransactionAmount("借入");
        double totalLend = dbHelper.getTotalTransactionAmount("借出");


        double totalBalance = totalIncome - totalExpense;

        totalBalanceTextView.setText("总收支: " + String.valueOf(totalBalance));
        List<SubAccountInfo> subAccountInfos = dbHelper.getSubAccountInfoList();
        adapter.setSubAccounts(subAccountInfos);
        totalBorrowTextView.setText(String.valueOf(totalBorrow));
        totalLendTextView.setText(String.valueOf(totalLend));


    }
    class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = verticalSpaceHeight; // 为第一个 item 添加顶部的间距
            }
        }
    }
}