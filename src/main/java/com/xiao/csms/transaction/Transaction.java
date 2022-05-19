package com.xiao.csms.transaction;

import eu.chargetime.ocpp.utilities.MoreObjects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="transaction")
public class Transaction implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "transactionId")
    int transactionId;

    @Column(name = "connectorId")
    int connectorId;

    @Column(name = "startTime")
    String startTime;

    @Column(name = "stopTime")
    String stopTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(int connectorId) {
        this.connectorId = connectorId;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

//    public Long getMeterStart() {
//        return meterStart;
//    }
//
//    public void setMeterStart(Long meterStart) {
//        this.meterStart = meterStart;
//    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

//    public Long getMeterStop() {
//        return meterStop;
//    }
//
//    public void setMeterStop(Long meterStop) {
//        this.meterStop = meterStop;
//    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this).add("transactionId",transactionId).add("connectorId", connectorId).add("start",startTime).add("stop",stopTime).toString();
    }
}
