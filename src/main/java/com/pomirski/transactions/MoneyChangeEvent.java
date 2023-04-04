package com.pomirski.transactions;

public class MoneyChangeEvent {
    private String accountId;
    private double moneyChange;

    public MoneyChangeEvent(String accountId, double moneyChange) {
        this.accountId = accountId;
        this.moneyChange = moneyChange;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getMoneyChange() {
        return moneyChange;
    }
}
