// TurfSearchDTO.java
package com.turfBooking.dto;

import com.turfBooking.enums.SportType;
import java.math.BigDecimal;

public class TurfSearchDTO {

    private String name;
    private String location;
    private SportType type;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    // Constructors
    public TurfSearchDTO() {}

    public TurfSearchDTO(String name, String location, SportType type, BigDecimal minPrice, BigDecimal maxPrice) {
        this.name = name;
        this.location = location;
        this.type = type;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public SportType getType() { return type; }
    public void setType(SportType type) { this.type = type; }

    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }

    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }
}