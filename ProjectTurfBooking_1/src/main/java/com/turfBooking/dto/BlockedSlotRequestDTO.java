// BlockedSlotRequestDTO.java
package com.turfBooking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class BlockedSlotRequestDTO {

    @NotNull(message = "Turf ID is required")
    @Positive(message = "Turf ID must be positive")
    private Long turfId;

    @NotNull(message = "Blocked date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate blockedDate;

    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    // Constructors
    public BlockedSlotRequestDTO() {}

    public BlockedSlotRequestDTO(Long turfId, LocalDate blockedDate, LocalTime startTime, LocalTime endTime) {
        this.turfId = turfId;
        this.blockedDate = blockedDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public Long getTurfId() { return turfId; }
    public void setTurfId(Long turfId) { this.turfId = turfId; }

    public LocalDate getBlockedDate() { return blockedDate; }
    public void setBlockedDate(LocalDate blockedDate) { this.blockedDate = blockedDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}
