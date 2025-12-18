package com.turfBooking.dto;

public class UserRegistrationDto {
    private String name;
    private String phone;
    private String password;
    private String registrationType; // "BOOK_SPORTS" or "OWN_TURF"

    // Default constructor
    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String name, String phone, String password, String registrationType) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.registrationType = registrationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }
}