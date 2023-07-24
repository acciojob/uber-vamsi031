package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;
    private String mobile;
    private String password;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<TripBooking> tripBookingsList = new ArrayList<>();

    public Customer() {
    }

    public Customer(int customerId, String mobie, String password, List<TripBooking> tripBookingsList) {
        this.customerId = customerId;
        this.mobile = mobie;
        this.password = password;
        this.tripBookingsList = tripBookingsList;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<TripBooking> getTripBookingsList() {
        return tripBookingsList;
    }

    public void setTripBookingsList(List<TripBooking> tripBookingsList) {
        this.tripBookingsList = tripBookingsList;
    }
}