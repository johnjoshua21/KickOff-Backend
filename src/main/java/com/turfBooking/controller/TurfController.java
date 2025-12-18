package com.turfBooking.controller;

import com.turfBooking.dto.TurfRequestDTO;
import com.turfBooking.dto.TurfResponseDTO;
import com.turfBooking.dto.TurfSearchDTO;
import com.turfBooking.dto.TurfUpdateDTO;
import com.turfBooking.enums.SportType;
import com.turfBooking.service.interfaces.TurfService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/turfs")
@CrossOrigin(origins = "*")
public class TurfController {

    @Autowired
    private TurfService turfService;

    // Create new turf
    @PostMapping
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<?> createTurf(@Valid @RequestBody TurfRequestDTO turfRequestDTO) {
        try {
            TurfResponseDTO createdTurf = turfService.createTurf(turfRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTurf);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Get all turfs
    @GetMapping
    public ResponseEntity<List<TurfResponseDTO>> getAllTurfs() {
        List<TurfResponseDTO> turfs = turfService.getAllTurfs();
        return ResponseEntity.ok(turfs);
    }

    // Get turf by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTurfById(@PathVariable Long id) {
        try {
            TurfResponseDTO turf = turfService.getTurfById(id);
            return ResponseEntity.ok(turf);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Update turf
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateTurf(@PathVariable Long id,
                                        @Valid @RequestBody TurfUpdateDTO turfUpdateDTO) {
        try {
            TurfResponseDTO updatedTurf = turfService.updateTurf(id, turfUpdateDTO);
            return ResponseEntity.ok(updatedTurf);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Delete turf
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteTurf(@PathVariable Long id) {
        try {
            turfService.deleteTurf(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Turf deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Get turfs by owner ID
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<TurfResponseDTO>> getTurfsByOwnerId(@PathVariable Long ownerId) {
        List<TurfResponseDTO> turfs = turfService.getTurfsByOwnerId(ownerId);
        return ResponseEntity.ok(turfs);
    }

    // Get turfs by sport type
    @GetMapping("/sport/{type}")
    public ResponseEntity<List<TurfResponseDTO>> getTurfsBySportType(@PathVariable SportType type) {
        List<TurfResponseDTO> turfs = turfService.getTurfsBySportType(type);
        return ResponseEntity.ok(turfs);
    }

    // Search turfs by location
    @GetMapping("/search/location")
    public ResponseEntity<List<TurfResponseDTO>> searchTurfsByLocation(@RequestParam String location) {
        List<TurfResponseDTO> turfs = turfService.searchTurfsByLocation(location);
        return ResponseEntity.ok(turfs);
    }

    // Search turfs by name
    @GetMapping("/search/name")
    public ResponseEntity<List<TurfResponseDTO>> searchTurfsByName(@RequestParam String name) {
        List<TurfResponseDTO> turfs = turfService.searchTurfsByName(name);
        return ResponseEntity.ok(turfs);
    }

    // Get turfs by price range
    @GetMapping("/search/price")
    public ResponseEntity<List<TurfResponseDTO>> getTurfsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<TurfResponseDTO> turfs = turfService.getTurfsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(turfs);
    }

    // Advanced search with multiple criteria
    @PostMapping("/search")
    public ResponseEntity<List<TurfResponseDTO>> searchTurfs(@RequestBody TurfSearchDTO searchDTO) {
        List<TurfResponseDTO> turfs = turfService.searchTurfs(searchDTO);
        return ResponseEntity.ok(turfs);
    }

    // Get available time slots for a turf on a specific date
    @GetMapping("/{id}/available-slots")
    public ResponseEntity<?> getAvailableTimeSlots(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<LocalTime> availableSlots = turfService.getAvailableTimeSlots(id, date);
            return ResponseEntity.ok(availableSlots);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Check if time slot is available
    @GetMapping("/{id}/check-availability")
    public ResponseEntity<Map<String, Boolean>> checkTimeSlotAvailability(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        boolean isAvailable = turfService.isTimeSlotAvailable(id, date, startTime, endTime);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isAvailable);
        return ResponseEntity.ok(response);
    }

    // Get turfs ordered by popularity (booking count)
    @GetMapping("/popular")
    public ResponseEntity<List<TurfResponseDTO>> getTurfsOrderedByPopularity() {
        List<TurfResponseDTO> turfs = turfService.getTurfsOrderedByPopularity();
        return ResponseEntity.ok(turfs);
    }

    // Get available turfs on a specific date
    @GetMapping("/available")
    public ResponseEntity<List<TurfResponseDTO>> getAvailableTurfsOnDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TurfResponseDTO> turfs = turfService.getAvailableTurfsOnDate(date);
        return ResponseEntity.ok(turfs);
    }

    // Check if turf name exists for owner
    @GetMapping("/check-name")
    public ResponseEntity<Map<String, Boolean>> checkTurfNameExists(
            @RequestParam String name,
            @RequestParam Long ownerId) {
        boolean exists = turfService.turfNameExistsForOwner(name, ownerId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // Get turf statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getTurfStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTurfs", turfService.getTotalTurfsCount());

        // Add count for each sport type
        for (SportType type : SportType.values()) {
            stats.put("total" + type.name() + "Turfs", turfService.getTurfsCountBySportType(type));
        }

        return ResponseEntity.ok(stats);
    }

    // Get owner's turf statistics
    @GetMapping("/statistics/owner/{ownerId}")
    public ResponseEntity<?> getOwnerTurfStatistics(@PathVariable Long ownerId) {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalTurfs", turfService.getTurfsCountByOwner(ownerId));
            stats.put("turfs", turfService.getTurfsByOwnerId(ownerId));
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Validate operating hours
    @PostMapping("/validate-hours")
    public ResponseEntity<Map<String, Boolean>> validateOperatingHours(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        boolean isValid = turfService.validateOperatingHours(startTime, endTime);
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }
}