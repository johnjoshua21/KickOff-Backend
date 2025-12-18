package com.turfBooking.dto;


public class JwtResponse {
    private final String jwttoken;
    private final String role;
    private final Long userId;

    public JwtResponse(String jwttoken, String role, Long userId) {
        this.jwttoken = jwttoken;
        this.role = role;
        this.userId = userId;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public String getRole() {
        return this.role;
    }

    public Long getUserId() {
        return this.userId;
    }
}