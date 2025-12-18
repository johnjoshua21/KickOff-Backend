package com.turfBooking.dto;

import com.turfBooking.enums.UserRole;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String phone;
    private UserRole role;
    private int totalBookings;
    private int totalTurfs;

    // Constructors
    public UserResponseDTO() {}

    public UserResponseDTO(Long id, String name, String phone, UserRole role) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public UserResponseDTO(Long id, String name, String phone, UserRole role, int totalBookings, int totalTurfs) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.totalBookings = totalBookings;
        this.totalTurfs = totalTurfs;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public int getTotalBookings() { return totalBookings; }
    public void setTotalBookings(int totalBookings) { this.totalBookings = totalBookings; }

    public int getTotalTurfs() { return totalTurfs; }
    public void setTotalTurfs(int totalTurfs) { this.totalTurfs = totalTurfs; }
}