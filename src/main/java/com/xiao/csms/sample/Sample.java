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

    @Column(name="temperature")
    int temperature;

    @Column(name="current")
    int current;

    @Column(name="voltage")
    int voltage;

    @Column(name="power")
    int power;

    @Column(name="energy")
    int energy;

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

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this).add("soc", this.soc).add("current",current).add("voltage",voltage).add("power",power).add("energy",energy).toString();
    }
}
