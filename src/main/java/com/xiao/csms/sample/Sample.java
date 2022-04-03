package com.xiao.csms.sample;

import eu.chargetime.ocpp.model.core.Location;
import eu.chargetime.ocpp.utilities.MoreObjects;

import javax.persistence.*;
import java.util.Arrays;

@Entity
public class Sample {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="transactionId", nullable = false)
    int transactionId;

    @Column(name="soc", nullable = false)
    int soc;

    @Column(name="tempature")
    String tempature;

    @Column(name="current")
    String current;

    @Column(name="voltage")
    String voltage;

    @Column(name="power")
    String power;

    @Column(name="energy")
    String energy;

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

    public int getSoc() {
        return soc;
    }

    public void setSoc(int soc) {
        this.soc = soc;
    }

    public String getTempature() {
        return tempature;
    }

    public void setTempature(String tempature) {
        this.tempature = tempature;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this).add("soc", this.soc).add("current",current).add("voltage",voltage).add("power",power).add("energy",energy).toString();
    }
}
