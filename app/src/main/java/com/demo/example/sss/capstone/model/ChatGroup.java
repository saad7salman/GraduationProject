package com.demo.example.sss.capstone.model;

/**
 * Created by SSS on 3/2/18.
 */

public class ChatGroup {

    public String chatID,chatDate,customerID,customerFullName;

    public ChatGroup(String chatID, String chatDate, String customerID, String customerFullName) {
        this.chatID = chatID;
        this.chatDate = chatDate;
        this.customerID = customerID;
        this.customerFullName = customerFullName;
    }
}
