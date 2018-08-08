package com.demo.example.sss.capstone.model;

/**
 * Created by SSS on 10/1/17.
 */

public class User {


    public String id,deliveryOnProgressID,orderID ,fullName,fullName2,type ,status,phoneNumber,
            latitude, longitude, delivStatus;

    public User(String deliveryOnProgressID,String fullName, String phoneNumber, String latitude, String longitude,String delivStatus) {
       this.deliveryOnProgressID = deliveryOnProgressID;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.delivStatus = delivStatus;
    }


    public User(String id,String fullName,String type,String status,String test){
        this.id = id;
        this.fullName = fullName;
        this.type = type;
        this.status = status;
    }

    public User(String deliveryOnProgressID, String orderID, String fullName, String fullName2) {
        this.deliveryOnProgressID = deliveryOnProgressID;
        this.orderID = orderID;
        this.fullName = fullName;
        this.fullName2 = fullName2;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
