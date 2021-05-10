package com.pluralsight.conferencedemo.models;

public class DataMessage {
    private int Id;
    private String message;

    public DataMessage(){

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
