package com.example.sarkaribook.Model;

public class Subscription {

    String monthText;
    String amountText;

    public Subscription(String monthText, String amountText) {
        this.monthText = monthText;
        this.amountText = amountText;
    }


    public String getMonthText() {
        return monthText;
    }

    public void setMonthText(String monthText) {
        this.monthText = monthText;
    }

    public String getAmountText() {
        return amountText;
    }

    public void setAmountText(String amountText) {
        this.amountText = amountText;
    }
}
