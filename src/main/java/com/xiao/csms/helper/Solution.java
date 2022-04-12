package com.xiao.csms.helper;

import eu.chargetime.ocpp.utilities.MoreObjects;

class Period{
    int startSoC, targetSoC;
    String startTime, endTime;

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this).add("startSoC", this.startSoC).add("targetSoC",targetSoC).add("startTime",startTime).add("endTime",endTime).toString();
    }
}

public class Solution {
    private int transactionId;
    private double totalPrice;

    // Depends on battery model
    private double capacity;
    // Max(Outlet.Power) of KemPower Connector, kW
    private double power;

    Period[] periods; // Time to start charging

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void init(String initTime, int initSoC){

    }
}
