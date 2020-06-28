package com.bills.exception;

public class NoBIllsRecordedException extends RuntimeException {

    private static final long serialVersionUID = -38231947418925908L;

    public NoBIllsRecordedException() {
        super("There is no recorded bill yet.");
    }
}
