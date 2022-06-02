package com.example.playtech;

public class Order {

    private int orderno;
    private String date;
    private double total;
    private String status;

    public Order() {
        this.orderno = 0;
        this.date = "";
        this.total = 0.0;
        this.status = "";
    }

    public Order(int orderno, String date, double total, String status) {
        this.orderno = orderno;
        this.date = date;
        this.total = total;
        this.status = status;
    }

    public int getOrderno() {
        return orderno;
    }

    public void setOrderno(int orderno) {
        this.orderno = orderno;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
