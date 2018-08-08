package com.demo.example.sss.capstone.model;

/**
 * Created by SSS on 2/17/18.
 */

public class Message {


    public String messageText;
    public String messageUser;
    public String messageTime;
    public String messageUserID;

    public Message(String messageText, String messageUser, String messageTime, String messageUserID) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = messageTime;
        this.messageUserID = messageUserID;
    }
}
