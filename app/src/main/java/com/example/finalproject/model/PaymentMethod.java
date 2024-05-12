package com.example.finalproject.model;

import java.io.Serializable;

public class PaymentMethod implements Serializable {
    public String cardName;
    public String cardNumber;
    public String cvv;
    public String paymentType;
    public String paymentName;

    public PaymentMethod(String cardName, String cardNumber, String cvv, String paymentType, String paymentName) {
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.paymentType = paymentType;
        this.paymentName = paymentName;
    }

    public PaymentMethod(String cardName, String cardNumber, String paymentType, String paymentName) {
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.paymentType = paymentType;
        this.paymentName = paymentName;
    }

    public PaymentMethod() {
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
}
