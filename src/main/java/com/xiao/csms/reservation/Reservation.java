package com.xiao.csms.reservation;

import eu.chargetime.ocpp.utilities.MoreObjects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="reservation")
public class Reservation {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "reservationId")
    int reservationId;

    @Column(name = "connectorId")
    int connectorId;

    @Column(name = "transactionId")
    int transactionId;

    @Column(name = "date")
    String date;

    @Column(name = "start")
    String start;

    @Column(name = "stop")
    String stop;

    @Column(name = "startSoC", nullable = false)
    int startSoC;

    @Column(name = "targetSoC", nullable = false)
    int targetSoC;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(int connectorId) {
        this.connectorId = connectorId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStartSoC() {
        return startSoC;
    }

    public void setStartSoC(int startSoC) {
        this.startSoC = startSoC;
    }

    public int getTargetSoC() {
        return targetSoC;
    }

    public void setTargetSoC(int targetSoC) {
        this.targetSoC = targetSoC;
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this).add("reservationId",reservationId).add("connectorId", connectorId).add("start",start).add("stop",stop).add("transactionId", transactionId).add("targetSoC",targetSoC).toString();
    }
}
