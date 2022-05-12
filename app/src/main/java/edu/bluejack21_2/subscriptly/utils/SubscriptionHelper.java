package edu.bluejack21_2.subscriptly.utils;

import java.util.ArrayList;
import java.util.Calendar;

import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;

public class SubscriptionHelper {
    public static Boolean isMonthYearTransactionExists(ArrayList<TransactionHeader> headers, TransactionHeader newHeader) {
        for (TransactionHeader transactionHeader:
                headers) {
            if(isSameDateField(transactionHeader.getBillingDate(), newHeader.getBillingDate(), Calendar.MONTH) && isSameDateField(transactionHeader.getBillingDate(), newHeader.getBillingDate(), Calendar.YEAR)) {
                return true;
            }
        }
        return false;
    }

    public static TransactionHeader getMonthYearTransactionExists(ArrayList<TransactionHeader> headers, TransactionHeader newHeader) {
        for (TransactionHeader transactionHeader:
                headers) {
            if(isSameDateField(transactionHeader.getBillingDate(), newHeader.getBillingDate(), Calendar.MONTH) && isSameDateField(transactionHeader.getBillingDate(), newHeader.getBillingDate(), Calendar.YEAR)) {
                return transactionHeader;
            }
        }
        return null;
    }

    public static TransactionDetail getUserPaidDetail(TransactionHeader header, String userId) {
        for (TransactionDetail detail:
             header.getDetails()) {
            if(detail.getUser().getUserID().equals(userId)) return detail;
        }
        return null;
    }

    public static Boolean isSameDateField(Calendar first, Calendar second, int field) {
        return first.get(field) == second.get(field);
    }
}
