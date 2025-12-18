package com.turfBooking.controller;

import com.turfBooking.dto.BlockedSlotRequestDTO;
import com.turfBooking.dto.BlockedSlotResponseDTO;
import com.turfBooking.service.interfaces.BlockedSlotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blocked-slots")
@CrossOrigin(origins = "*")public class BlockedSlotController {

    @Autowired
    private BlockedSlotService blockedSlotService;

    // Create new blocked slot
    @PostMapping
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<?> createBlockedSlot(@Valid @RequestBody BlockedSlotRequestDTO requestDTO) {
        try {
            BlockedSlotResponseDTO createdBlockedSlot = blockedSlotService.createBlockedSlot(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBlockedSlot);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Get all blocked slots
    @GetMapping
    public ResponseEntity<List<BlockedSlotResponseDTO>> getAllBlockedSlots() {
        List<BlockedSlotResponseDTO> blockedSlots = blockedSlotService.getAllBlockedSlots();
        return ResponseEntity.ok(blockedSlots);
    }

    // Get blocked slot by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBlockedSlotById(@PathVariable Long id) {
        try {
            BlockedSlotResponseDTO blockedSlot = blockedSlotService.getBlockedSlotById(id);
            return ResponseEntity.ok(blockedSlot);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Delete blocked slot
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteBlockedSlot(@PathVariable Long id) {
        try {
            blockedSlotService.deleteBlockedSlot(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Blocked slot deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Get blocked slots by turf ID
    @GetMapping("/turf/{turfId}")
    public ResponseEntity<List<BlockedSlotResponseDTO>> getBlockedSlotsByTurfId(@PathVariable Long turfId) {
        List<BlockedSlotResponseDTO> blockedSlots = blockedSlotService.getBlockedSlotsByTurfId(turfId);
        return ResponseEntity.ok(blockedSlots);
    }

    // Get blocked slots by turf ID and date
    @GetMapping("/turf/{turfId}/date/{date}")
    public ResponseEntity<List<BlockedSlotResponseDTO>> getBlockedSlotsByTurfIdAndDate(
            @PathVariable Long turfId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<BlockedSlotResponseDTO> blockedSlots = blockedSlotService.getBlockedSlotsByTurfIdAndDate(turfId, date);
        return ResponseEntity.ok(blockedSlots);
    }

    // Get blocked slots by date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<BlockedSlotResponseDTO>> getBlockedSlotsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<BlockedSlotResponseDTO> blockedSlots = blockedSlotService.getBlockedSlotsByDate(date);
        return ResponseEntity.ok(blockedSlots);
    }

    // Get blocked slots by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<BlockedSlotResponseDTO>> getBlockedSlotsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<BlockedSlotResponseDTO> blockedSlots = blockedSlotService.getBlockedSlotsByDateRange(startDate, endDate);
        return ResponseEntity.ok(blockedSlots);
    }

    // Get blocked slots by turf owner ID
    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<List<BlockedSlotResponseDTO>> getBlockedSlotsByTurfOwnerId(@PathVariable Long ownerId) {
        List<BlockedSlotResponseDTO> blockedSlots = blockedSlotService.getBlockedSlotsByTurfOwnerId(ownerId);
        return ResponseEntity.ok(blockedSlots);
    }

    // Get future blocked slots for a turf
    @GetMapping("/turf/{turfId}/future")
    public ResponseEntity<List<BlockedSlotResponseDTO>> getFutureBlockedSlotsByTurfId(@PathVariable Long turfId) {
        List<BlockedSlotResponseDTO> blockedSlots = blockedSlotService.getFutureBlockedSlotsByTurfId(turfId);
        return ResponseEntity.ok(blockedSlots);
    }

    // Cleanup old blocked slots (can be called manually or scheduled)
    @DeleteMapping("/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cleanupOldBlockedSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beforeDate) {
        try {
            blockedSlotService.cleanupOldBlockedSlots(beforeDate);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Old blocked slots cleaned up successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}