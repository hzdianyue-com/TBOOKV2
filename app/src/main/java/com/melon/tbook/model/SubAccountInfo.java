package com.melon.tbook.model;

public class SubAccountInfo {
    private String subAccountName;
    private double totalIncome;
    private double totalExpense;

    public SubAccountInfo(String subAccountName, double totalIncome, double totalExpense) {
        this.subAccountName = subAccountName;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
    }

    public String getSubAccountName() {
        return subAccountName;
    }

    public void setSubAccountName(String subAccountName) {
        this.subAccountName = subAccountName;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }
}