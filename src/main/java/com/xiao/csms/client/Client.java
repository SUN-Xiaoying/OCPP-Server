package com.xiao.csms.client;

import com.xiao.csms.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.sql.Date;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="client")
public class Client {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "connectorId")
    int connectorId;

    @Column(name = "transactionId")
    int transactionId;

    @Column(name = "timeStamp")
    String timeStamp;

    @Column(name = "reservationId")
    int reservationId;

    @Column(name = "idTag")
    String idTag;

    @Column(name = "meterStart")
    int meterStart;

    @Column(name = "meterStop")
    int meterStop;

    public void setId(int id) {
        this.id = id;
    }

    public int getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(int connectorId) {
        this.connectorId = connectorId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getIdTag() {
        return idTag;
    }

    public void setIdTag(String idTag) {
        this.idTag = idTag;
    }

    public int getMeterStart() {
        return meterStart;
    }

    public void setMeterStart(int meterStart) {
        this.meterStart = meterStart;
    }

    public int getMeterStop() {
        return meterStop;
    }

    public void setMeterStop(int meterStop) {
        this.meterStop = meterStop;
    }

    @Override
    public String toString(){
        return "Client{\n"+
                "id = " + id + "\n" +
                "connector = " + connectorId + "\n" +
                "transaction = " + transactionId + "\n" +
                "timestamp = " + timeStamp + "\n" +
                "}";
    }
}
