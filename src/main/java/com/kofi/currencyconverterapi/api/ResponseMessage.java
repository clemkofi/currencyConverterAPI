package com.kofi.currencyconverterapi.api;

public enum ResponseMessage {

    SUCCESS_MESSAGE("1", "Operation Success!"),
    FAILURE_MESSAGE("0", "Operation Failed!");

    private final String id;
    private final String message;

    //Enum constructor
    ResponseMessage(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
