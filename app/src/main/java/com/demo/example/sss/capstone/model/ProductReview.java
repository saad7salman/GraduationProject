package com.demo.example.sss.capstone.model;

/**
 * Created by SSS on 4/7/18.
 */

public class ProductReview {

    public String productID,customerName,createdDate,rating,comment;

    public ProductReview(String productID, String customerName, String createdDate, String rating, String comment) {
        this.productID = productID;
        this.customerName = customerName;
        this.createdDate = createdDate;
        this.rating = rating;
        this.comment = comment;
    }
}
