package com.xiao.csms.reservation;

import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@NoArgsConstructor
public class Comparison {
    int transactionId;
    double currentCost;
    double smartCost;
    double profit;
    String currentEndTime;
    String smartEndTime;
    double delay;

    DecimalFormat df = new DecimalFormat("0.000");

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public double getCurrentCost() {
        return currentCost;
    }

    public void setCurrentCost(double currentCost) {this.currentCost = Double.parseDouble(df.format(currentCost));}

    public double getSmartCost() {
        return smartCost;
    }

    public void setSmartCost(double smartCost) {
        this.smartCost = Double.parseDouble(df.format(smartCost));
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit() {
        this.profit = currentCost-smartCost;
    }

    public String getCurrentEndTime() {
        return currentEndTime;
    }

    public void setCurrentEndTime(String currentEndTime) {
        this.currentEndTime = currentEndTime;
    }

    public String getSmartEndTime() {
        return smartEndTime;
    }

    public void setSmartEndTime(String smartEndTime) {
        this.smartEndTime = smartEndTime;
    }

    public double getDelay() {
        return delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }
}
