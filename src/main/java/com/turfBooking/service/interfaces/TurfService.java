package com.turfBooking.service.interfaces;

import com.turfBooking.dto.TurfRequestDTO;
import com.turfBooking.dto.TurfResponseDTO;
import com.turfBooking.dto.TurfUpdateDTO;
import com.turfBooking.dto.TurfSearchDTO;
import com.turfBooking.enums.SportType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TurfService {

    // Create new turf
    TurfResponseDTO createTurf(TurfRequestDTO turfRequestDTO);

    // Get turf by ID
    TurfResponseDTO getTurfById(Long id);

    // Get all turfs
    List<TurfResponseDTO> getAllTurfs();

    // Update turf
    TurfResponseDTO updateTurf(Long id, TurfUpdateDTO turfUpdateDTO);

    // Delete turf
    void deleteTurf(Long id);

    // Get turfs by owner ID
    List<TurfResponseDTO> getTurfsByOwnerId(Long ownerId);

    // Get turfs by sport type
    List<TurfResponseDTO> getTurfsBySportType(SportType type);

    // Search turfs by location
    List<TurfResponseDTO> searchTurfsByLocation(String location);

    // Search turfs by name
    List<TurfResponseDTO> searchTurfsByName(String name);

    // Get turfs by price range
    List<TurfResponseDTO> getTurfsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    // Advanced search with multiple criteria
    List<TurfResponseDTO> searchTurfs(TurfSearchDTO searchDTO);

    // Get available time slots for a turf on a specific date
    List<LocalTime> getAvailableTimeSlots(Long turfId, LocalDate date);

    // Check if time slot is available
    boolean isTimeSlotAvailable(Long turfId, LocalDate date, LocalTime startTime, LocalTime endTime);

    // Get turfs ordered by popularity (booking count)
    List<TurfResponseDTO> getTurfsOrderedByPopularity();

    // Get available turfs on a specific date
    List<TurfResponseDTO> getAvailableTurfsOnDate(LocalDate date);

    // Get total turfs count
    long getTotalTurfsCount();

    // Get turfs count by sport type
    long getTurfsCountBySportType(SportType type);

    // Get turfs count by owner
    long getTurfsCountByOwner(Long ownerId);

    // Validate turf operating hours
    boolean validateOperatingHours(LocalTime startTime, LocalTime endTime);

    // Check if turf name exists for owner
    boolean turfNameExistsForOwner(String name, Long ownerId);
}