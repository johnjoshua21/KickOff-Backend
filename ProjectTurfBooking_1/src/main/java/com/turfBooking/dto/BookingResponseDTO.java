// BookingResponseDTO.java
package com.turfBooking.dto;

import com.turfBooking.enums.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingResponseDTO {

    private Long id;
    private LocalTime slotStartTime;
    private LocalTime slotEndTime;
    private LocalDate bookingDate;
    private BookingStatus status;

    // User details
    private Long userId;
    private String userName;
    private String userPhone;

    // Turf details
    private Long turfId;
    private String turfName;
    private String turfLocation;
    private String turfType;
    private BigDecimal turfPrice;
    private String turfOwnerName;
    private String turfOwnerPhone;

    // Calculated fields
    private BigDecimal totalPrice;
    private long durationHours;

    // Constructors
    public BookingResponseDTO() {}

    public BookingResponseDTO(Long id, LocalTime slotStartTime, LocalTime slotEndTime,
                              LocalDate bookingDate, BookingStatus status,
                              Long userId, String userName, String userPhone,
                              Long turfId, String turfName, String turfLocation,
                              String turfType, BigDecimal turfPrice,
                              String turfOwnerName, String turfOwnerPhone) {
        this.id = id;
        this.slotStartTime = slotStartTime;
        this.slotEndTime = slotEndTime;
        this.bookingDate = bookingDate;
        this.status = status;
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.turfId = turfId;
        this.turfName = turfName;
        this.turfLocation = turfLocation;
        this.turfType = turfType;
        this.turfPrice = turfPrice;
        this.turfOwnerName = turfOwnerName;
        this.turfOwnerPhone = turfOwnerPhone;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalTime getSlotStartTime() { return slotStartTime; }
    public void setSlotStartTime(LocalTime slotStartTime) { this.slotStartTime = slotStartTime; }

    public LocalTime getSlotEndTime() { return slotEndTime; }
    public void setSlotEndTime(LocalTime slotEndTime) { this.slotEndTime = slotEndTime; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public Long getTurfId() { return turfId; }
    public void setTurfId(Long turfId) { this.turfId = turfId; }

    public String getTurfName() { return turfName; }
    public void setTurfName(String turfName) { this.turfName = turfName; }

    public String getTurfLocation() { return turfLocation; }
    public void setTurfLocation(String turfLocation) { this.turfLocation = turfLocation; }

    public String getTurfType() { return turfType; }
    public void setTurfType(String turfType) { this.turfType = turfType; }

    public BigDecimal getTurfPrice() { return turfPrice; }
    public void setTurfPrice(BigDecimal turfPrice) { this.turfPrice = turfPrice; }

    public String getTurfOwnerName() { return turfOwnerName; }
    public void setTurfOwnerName(String turfOwnerName) { this.turfOwnerName = turfOwnerName; }

    public String getTurfOwnerPhone() { return turfOwnerPhone; }
    public void setTurfOwnerPhone(String turfOwnerPhone) { this.turfOwnerPhone = turfOwnerPhone; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public long getDurationHours() { return durationHours; }
    public void setDurationHours(long durationHours) { this.durationHours = durationHours; }
}

