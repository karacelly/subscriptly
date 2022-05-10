package edu.bluejack21_2.subscriptly.models;

import java.util.ArrayList;
import java.util.Calendar;

public class TransactionHeader {
    private String transactionId;
    private Calendar billingDate;
    private ArrayList<TransactionDetail> details;

    public TransactionHeader(String transactionId, Calendar billingDate, ArrayList<TransactionDetail> details) {
        this.transactionId = transactionId;
        this.billingDate = billingDate;
        this.details = details;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Calendar getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(Calendar billingDate) {
        this.billingDate = billingDate;
    }

    public ArrayList<TransactionDetail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<TransactionDetail> details) {
        this.details = details;
    }
}
