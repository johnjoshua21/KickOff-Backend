package com.turfBooking.entity;

import com.turfBooking.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;



    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50)
    private String name;



    @NotBlank(message = "Phone Number is required")
    @Size(min = 10, max = 14)
    private String phone;




    @NotBlank(message = "Password is required")
    @Size(min = 6)
    private String password;



    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;




    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;



    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Turf> turfs;



    // Constructors
    public User() {}

    public User(String name, String phone, String password, UserRole role) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.role = role;
        //this.isVerified = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

//    public boolean isVerified() { return isVerified; }
//    public void setVerified(boolean verified) { isVerified = verified; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public List<Turf> getTurfs() { return turfs; }
    public void setTurfs(List<Turf> turfs) { this.turfs = turfs; }



}
