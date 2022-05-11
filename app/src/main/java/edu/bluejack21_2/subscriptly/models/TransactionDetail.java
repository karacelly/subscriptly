package edu.bluejack21_2.subscriptly.models;

import java.util.Calendar;

public class TransactionDetail {
    private String transactionDetailId, image;
    private User user;
    private Boolean verified;
    private Calendar paymentDate;

    public TransactionDetail(String transactionDetailId, String image, User user, Boolean verified, Calendar paymentDate) {
        this.transactionDetailId = transactionDetailId;
        this.image = image;
        this.user = user;
        this.verified = verified;
        this.paymentDate = paymentDate;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getTransactionDetailId() {
        return transactionDetailId;
    }

    public void setTransactionDetailId(String transactionDetailId) {
        this.transactionDetailId = transactionDetailId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Calendar getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Calendar paymentDate) {
        this.paymentDate = paymentDate;
    }
}
