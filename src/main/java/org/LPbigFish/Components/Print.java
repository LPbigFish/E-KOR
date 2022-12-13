package org.LPbigFish.Components;

import java.math.BigInteger;
import java.util.concurrent.Callable;

public class Print implements Callable<SubBill> {

    private SubBill bill;
    private final BigInteger target;

    public Print(SubBill bill, BigInteger target) {
        this.bill = bill;
        this.target = target;
    }

    @Override
    public SubBill call() {

        bill = new SubBill(bill.authorityHash(), bill.data(), bill.signature(), bill.nonce(), bill.printerAddress());
        while (new BigInteger(bill.authorityHash(), 16).compareTo(target) >= 0) {
            bill = new SubBill(bill.authorityHash(), bill.data(), bill.signature(), bill.nonce() + 1, bill.printerAddress());
        }

        return new SubBill(bill.authorityHash(), bill.data(), bill.signature(), bill.nonce(), bill.printerAddress());
    }

}
