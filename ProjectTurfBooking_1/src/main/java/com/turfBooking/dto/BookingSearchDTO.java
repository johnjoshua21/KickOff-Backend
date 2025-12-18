// BookingSearchDTO.java
package com.turfBooking.dto;

import com.turfBooking.enums.BookingStatus;
import java.time.LocalDate;

public class BookingSearchDTO {

    private Long userId;
    private Long turfId;
    private BookingStatus status;
    private LocalDate startDate;
    private LocalDate endDate;

    // Constructors
    public BookingSearchDTO() {}

    public BookingSearchDTO(Long userId, Long turfId, BookingStatus status,
                            LocalDate startDate, LocalDate endDate) {
        this.userId = userId;
        this.turfId = turfId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getTurfId() { return turfId; }
    public void setTurfId(Long turfId) { this.turfId = turfId; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}