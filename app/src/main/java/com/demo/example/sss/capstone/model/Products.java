package com.demo.example.sss.capstone.model;

import android.util.Log;

import com.android.volley.toolbox.StringRequest;
import com.demo.example.sss.capstone.Provider.ProviderCusRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SSS on 10/12/17.
 */

public class Products {

    public String productID;
    public String name;
    public String description;
    public double price;
    public String imagePath;
    public String category;
    public String provid;
    public double distanceP;
    public String providerName;
    public String customerName;
    public String quantity;
    public String cartID;
    public String orderNumber;
    public Date date;
    public String status;
    public String address;
    public String phoneNumber;
    public String delivOption;
    public String estimationTime;
    public String maxQuantity;
    public String specialRequest;
    public String cusID;
    public String rating;
    public Products() {

    }

    public Products(String productID,String name, String price, String imagePath, String orderNumber, String quantity,
                    String date, String providerName, String status, String address, String phoneNumber,
                    String delivOption,String rating) {
        this.productID =productID;
        this.name = name;
        this.price = Double.parseDouble(price);
        this.imagePath = imagePath;
        this.orderNumber = orderNumber;
        this.quantity = quantity;
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
            this.date = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.providerName = providerName;
        this.status = status;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.delivOption = delivOption;
        this.rating = rating;
    }

    public Products(String productID,String name, String description, String price,
                    String category,String imagePath, String provid,double distanceP,
                    String estimationTime,String maxQuantity,String rating) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.price = Double.parseDouble(price);
        this.imagePath = imagePath;
        this.category = category;
        this.provid = provid;
        this.distanceP = distanceP;
        this.estimationTime = estimationTime;
        this.maxQuantity = maxQuantity;
        this.rating = rating;
    }

    public Products(String productID,String name, String description, String price,String estimationTime,String maxQuantity,
                    String category,String imagePath) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.price = Double.parseDouble(price);
        this.imagePath = imagePath;
        this.category = category;
        this.estimationTime = estimationTime;
        this.maxQuantity = maxQuantity;
    }

    public Products(String cartID,String name , String price,String imagePath,
                    String providerName,String quantity, String provid) {
        this.cartID = cartID;
        this.name = name;
        this.price = Double.parseDouble(price);
        this.imagePath = imagePath;
        this.provid = provid;
        this.providerName = providerName;
        this.quantity = quantity;

    }

    public Products(String OrderNum,String ProdName , String quantity,String status,
                    String customerName,String imagePath){

        this.orderNumber = OrderNum;
        this.name = ProdName;
        this.quantity = quantity;
        this.status = status;
        this.customerName = customerName;
        this.imagePath = imagePath;
    }

    public Products(String name, String quantity,String orderNumber,String imagePath){
       this.name = name;
       this.quantity = quantity;
       this.orderNumber = orderNumber;
       this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Products{" +
                "productID='" + productID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imagePath='" + imagePath + '\'' +
                ", category='" + category + '\'' +
                ", provid='" + provid + '\'' +
                ", distanceP=" + distanceP +
                ", providerName='" + providerName + '\'' +
                ", quantity='" + quantity + '\'' +
                ", cartID='" + cartID + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", address='" + address + '\'' +
                '}';

    }
}
