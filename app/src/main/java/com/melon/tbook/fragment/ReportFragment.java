package com.melon.tbook.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.melon.tbook.R;
import com.melon.tbook.model.Transaction;
import com.melon.tbook.utils.DataProxy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportFragment extends Fragment {
    private PieChart pieChart;
    private Spinner spinnerYear;
    private Spinner spinnerMonth;
    private DataProxy dbHelper;


    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = DataProxy.getInstance(getContext());
        pieChart = view.findViewById(R.id.pie_chart);
        spinnerYear = view.findViewById(R.id.spinner_year);
        spinnerMonth = view.findViewById(R.id.spinner_month);
        initSpinners();
        loadDataForChart();
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadDataForChart();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadDataForChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSpinners(){
        // 初始化年份 Spinner
        List<String> yearList = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i = currentYear; i>= 2000; i--){
            yearList.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);
        // 初始化月份 Spinner
        List<String> monthList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            monthList.add(String.format(Locale.getDefault(),"%02d", i));
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, monthList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);
    }
    private void loadDataForChart() {
        String selectedYear = spinnerYear.getSelectedItem().toString();
        String selectedMonth = spinnerMonth.getSelectedItem().toString();
        List<Transaction> transactions = dbHelper.getAllTransactions();
        double totalIncome = 0;
        double totalExpense = 0;

        for(Transaction transaction: transactions){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try{
                Date date = sdf.parse(String.valueOf(calendar.getTime()));
                if(date !=null){
                    calendar.setTime(date);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            int transactionYear = calendar.get(Calendar.YEAR);
            String transactionMonth = String.format(Locale.getDefault(),"%02d",calendar.get(Calendar.MONTH)+1);
            if(!String.valueOf(transactionYear).equals(selectedYear)){
                continue;
            }

            if(!selectedMonth.equals("00") && !transactionMonth.equals(selectedMonth)){
                continue;
            }

            if(transaction.getType().equals("收入")){
                totalIncome += transaction.getAmount();
            }else if(transaction.getType().equals("支出")){
                totalExpense += transaction.getAmount();
            }
        }

        List<PieEntry> entries = new ArrayList<>();
        if (totalIncome > 0) {
            entries.add(new PieEntry((float) totalIncome, "收入"));
        }
        if (totalExpense > 0) {
            entries.add(new PieEntry((float) totalExpense, "支出"));
        }
        if(entries.isEmpty()){
            pieChart.clear();
            pieChart.invalidate();
            return;
        }
        PieDataSet dataSet = new PieDataSet(entries, "收支类型");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("收支");
        pieChart.invalidate();
    }
}