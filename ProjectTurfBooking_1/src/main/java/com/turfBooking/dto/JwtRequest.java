package com.turfBooking.dto;

public class JwtRequest {
    private String phone;
    private String password;

    // Default constructor for JSON Parsing
    public JwtRequest() {
    }

    public JwtRequest(String phone, String password) {
        this.setPhone(phone);
        this.setPassword(password);
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}