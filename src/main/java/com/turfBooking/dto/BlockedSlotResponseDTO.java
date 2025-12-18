// BlockedSlotResponseDTO.java
package com.turfBooking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class BlockedSlotResponseDTO {

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate blockedDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    // Turf details
    private Long turfId;
    private String turfName;
    private String turfLocation;

    // Constructors
    public BlockedSlotResponseDTO() {}

    public BlockedSlotResponseDTO(Long id, LocalDate blockedDate, LocalTime startTime, LocalTime endTime,
                                  Long turfId, String turfName, String turfLocation) {
        this.id = id;
        this.blockedDate = blockedDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.turfId = turfId;
        this.turfName = turfName;
        this.turfLocation = turfLocation;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getBlockedDate() { return blockedDate; }
    public void setBlockedDate(LocalDate blockedDate) { this.blockedDate = blockedDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public Long getTurfId() { return turfId; }
    public void setTurfId(Long turfId) { this.turfId = turfId; }

    public String getTurfName() { return turfName; }
    public void setTurfName(String turfName) { this.turfName = turfName; }

    public String getTurfLocation() { return turfLocation; }
    public void setTurfLocation(String turfLocation) { this.turfLocation = turfLocation; }
}
