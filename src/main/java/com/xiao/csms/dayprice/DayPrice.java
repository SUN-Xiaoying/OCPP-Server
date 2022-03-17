package com.xiao.csms.dayprice;

import javax.persistence.*;

@Entity
@Table(name="dayprice")
public class DayPrice {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date")
    private String date;

    @Column(name = "hour")
    private int hour;

    @Column(name = "price")
    private double price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString(){
        return "Dayprice{\n"+
                "id = " + id + "\n" +
                "date = " + date + " " + hour + "\n" +
                "price = " + price + "\n" +
                "}";
    }
}
