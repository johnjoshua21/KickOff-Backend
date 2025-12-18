// BookingUpdateDTO.java
package com.turfBooking.dto;

import com.turfBooking.enums.BookingStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingUpdateDTO {

    private LocalTime slotStartTime;
    private LocalTime slotEndTime;
    private LocalDate bookingDate;
    private BookingStatus status;

    // Constructors
    public BookingUpdateDTO() {}

    public BookingUpdateDTO(LocalTime slotStartTime, LocalTime slotEndTime,
                            LocalDate bookingDate, BookingStatus status) {
        this.slotStartTime = slotStartTime;
        this.slotEndTime = slotEndTime;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    // Getters and Setters
    public LocalTime getSlotStartTime() { return slotStartTime; }
    public void setSlotStartTime(LocalTime slotStartTime) { this.slotStartTime = slotStartTime; }

    public LocalTime getSlotEndTime() { return slotEndTime; }
    public void setSlotEndTime(LocalTime slotEndTime) { this.slotEndTime = slotEndTime; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}

