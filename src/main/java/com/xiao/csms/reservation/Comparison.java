package com.xiao.csms.reservation;

import com.xiao.csms.helper.Helper;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@NoArgsConstructor
public class Comparison {
    int reservationId;
    double uncontrolledCost;
    double smartCost;
    double profit;
    String actualTime;
    String estimatedTime;
    String ape;
    private static DecimalFormat df = new DecimalFormat("0.000");

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public double getUncontrolledCost() {
        return uncontrolledCost;
    }

    public void setUncontrolledCost(double uncontrolledCost) {
        this.uncontrolledCost = Double.parseDouble(df.format(uncontrolledCost));
    }

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
        this.profit = Double.parseDouble(df.format(uncontrolledCost - smartCost));
    }

    public String getActualTime() {
        return actualTime;
    }

    public void setActualTime(String actualTime) {
        this.actualTime = actualTime;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getApe() {
        return ape;
    }

    public void setApe() {
        this.ape = Helper.ape(this.actualTime, this.estimatedTime);
    }
}
