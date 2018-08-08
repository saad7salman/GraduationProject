package com.demo.example.sss.capstone.model;

/**
 * Created by SSS on 10/14/17.
 */

public class Addresses {


    public String addressline;
    public String city;
    public String state;
    public String zipcode;
    public String country;


    public Addresses(String addressline, String city, String state, String zipcode, String country) {
        this.addressline = addressline;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
    }


    @Override
    public String toString() {
        return "Addresses{" +
                "addressline='" + addressline + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
