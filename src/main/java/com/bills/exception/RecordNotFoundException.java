package com.bills.exception;

public class RecordNotFoundException extends RuntimeException {


    private static final long serialVersionUID = 2834316862312517375L;

    public RecordNotFoundException(Long id) {
        super("No record found for id : " + id);
    }
}
