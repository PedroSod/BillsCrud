package com.bills.exception;

public class BillInterestRulesNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3244236110056980710L;

    public BillInterestRulesNotFoundException() {
        super("Interest bill rule not found.");
    }
}
