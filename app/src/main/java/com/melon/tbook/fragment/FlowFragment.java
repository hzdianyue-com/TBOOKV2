package com.melon.tbook.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.melon.tbook.R;
import com.melon.tbook.adapter.TransactionAdapter;
import com.melon.tbook.model.Transaction;
import com.melon.tbook.utils.DatabaseHelper;

import java.util.List;


public class FlowFragment extends Fragment implements TransactionAdapter.OnTransactionClickListener {
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private DatabaseHelper dbHelper;
    private TextView noTransactionsText;


    public FlowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_transactions);
        noTransactionsText = view.findViewById(R.id.text_no_transactions);

        dbHelper = new DatabaseHelper(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // 添加 ItemDecoration 设置 item 间距
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(16));
        adapter = new TransactionAdapter(dbHelper.getAllTransactions());
        adapter.setTransactionClickListener(this);
        recyclerView.setAdapter(adapter);

        updateTransactionList();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTransactionList();
    }
    private void updateTransactionList() {
        if(getContext() == null){
            return;
        }
        List<Transaction> transactions = dbHelper.getAllTransactions();
        if (transactions.isEmpty()) {
            noTransactionsText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noTransactionsText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setTransactions(transactions);
        }
    }

    @Override
    public void onTransactionClick(Transaction transaction) {
        showDeleteConfirmationDialog(transaction);
    }

    private void showDeleteConfirmationDialog(Transaction transaction) {
        if (getContext() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.title_confirm_delete);
        builder.setMessage(R.string.message_confirm_delete_transaction);
        builder.setPositiveButton(R.string.button_delete, (dialog, which) -> {
            dbHelper.deleteTransaction(transaction.getId());
            Toast.makeText(requireContext(), R.string.message_transaction_deleted, Toast.LENGTH_SHORT).show();
            updateTransactionList();
        });
        builder.setNegativeButton(R.string.button_cancel, null);
        builder.show();
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
                outRect.top = verticalSpaceHeight;
            }
        }
    }
}