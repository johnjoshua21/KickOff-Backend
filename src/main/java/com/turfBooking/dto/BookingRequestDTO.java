// BookingRequestDTO.java
package com.turfBooking.dto;

import com.turfBooking.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingRequestDTO {

    @NotNull(message = "Start time is required")
    private LocalTime slotStartTime;

    @NotNull(message = "End time is required")
    private LocalTime slotEndTime;

    @NotNull(message = "Booking date is required")
    private LocalDate bookingDate;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Turf ID is required")
    private Long turfId;

    private BookingStatus status = BookingStatus.CONFIRMED;

    // Constructors
    public BookingRequestDTO() {}

    public BookingRequestDTO(LocalTime slotStartTime, LocalTime slotEndTime, LocalDate bookingDate,
                             Long userId, Long turfId, BookingStatus status) {
        this.slotStartTime = slotStartTime;
        this.slotEndTime = slotEndTime;
        this.bookingDate = bookingDate;
        this.userId = userId;
        this.turfId = turfId;
        this.status = status;
    }

    // Getters and Setters
    public LocalTime getSlotStartTime() { return slotStartTime; }
    public void setSlotStartTime(LocalTime slotStartTime) { this.slotStartTime = slotStartTime; }

    public LocalTime getSlotEndTime() { return slotEndTime; }
    public void setSlotEndTime(LocalTime slotEndTime) { this.slotEndTime = slotEndTime; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getTurfId() { return turfId; }
    public void setTurfId(Long turfId) { this.turfId = turfId; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}

