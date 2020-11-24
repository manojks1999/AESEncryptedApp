package com.example.encr;

public class List_Data {
    private String Name;
    private String Message;


    public List_Data() {
    }

    public List_Data(String name, String message) {
        Name = name;
        Message = message;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
