package com.turfBooking.dto;

import jakarta.validation.constraints.Size;

public class UserUpdateDTO {

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Size(min = 10, max = 14, message = "Phone number must be between 10 and 14 characters")
    private String phone;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    // Constructors
    public UserUpdateDTO() {}

    public UserUpdateDTO(String name, String phone, String password) {
        this.name = name;
        this.phone = phone;
        this.password = password;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}