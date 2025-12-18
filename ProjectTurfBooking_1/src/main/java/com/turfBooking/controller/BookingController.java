package com.turfBooking.controller;

import com.turfBooking.dto.BookingRequestDTO;
import com.turfBooking.dto.BookingResponseDTO;
import com.turfBooking.dto.BookingUpdateDTO;
import com.turfBooking.dto.BookingSearchDTO;
import com.turfBooking.enums.BookingStatus;
import com.turfBooking.service.interfaces.BookingService;
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
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Create new booking
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequestDTO bookingRequestDTO) {
        try {
            BookingResponseDTO createdBooking = bookingService.createBooking(bookingRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Get all bookings
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        List<BookingResponseDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // Get booking by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        try {
            BookingResponseDTO booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Update booking
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateBooking(@PathVariable Long id,
                                           @Valid @RequestBody BookingUpdateDTO bookingUpdateDTO) {
        try {
            BookingResponseDTO updatedBooking = bookingService.updateBooking(id, bookingUpdateDTO);
            return ResponseEntity.ok(updatedBooking);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Cancel booking
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        try {
            BookingResponseDTO cancelledBooking = bookingService.cancelBooking(id);
            return ResponseEntity.ok(cancelledBooking);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Delete booking
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Booking deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Get bookings by user ID
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")

    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUserId(@PathVariable Long userId) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    // Get bookings by turf ID
    @GetMapping("/turf/{turfId}")
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByTurfId(@PathVariable Long turfId) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByTurfId(turfId);
        return ResponseEntity.ok(bookings);
    }

    // Get bookings by status
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<List<BookingResponseDTO>> getBookingsByStatus(@PathVariable BookingStatus status) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByStatus(status);
        return ResponseEntity.ok(bookings);
    }

    // Get bookings by date
    @GetMapping("/date/{date}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByDate(date);
        return ResponseEntity.ok(bookings);
    }

    // Get bookings by date range
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByDateRange(startDate, endDate);
        return ResponseEntity.ok(bookings);
    }

    // Get upcoming bookings for user
    @GetMapping("/user/{userId}/upcoming")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDTO>> getUpcomingBookingsForUser(@PathVariable Long userId) {
        List<BookingResponseDTO> bookings = bookingService.getUpcomingBookingsForUser(userId);
        return ResponseEntity.ok(bookings);
    }

    // Get upcoming bookings for turf
    @GetMapping("/turf/{turfId}/upcoming")
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDTO>> getUpcomingBookingsForTurf(@PathVariable Long turfId) {
        List<BookingResponseDTO> bookings = bookingService.getUpcomingBookingsForTurf(turfId);
        return ResponseEntity.ok(bookings);
    }

    // Get bookings for turf owner (all their turfs)
    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsForTurfOwner(@PathVariable Long ownerId) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsForTurfOwner(ownerId);
        return ResponseEntity.ok(bookings);
    }

    // Get upcoming bookings for turf owner
    @GetMapping("/owner/{ownerId}/upcoming")
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDTO>> getUpcomingBookingsForTurfOwner(@PathVariable Long ownerId) {
        List<BookingResponseDTO> bookings = bookingService.getUpcomingBookingsForTurfOwner(ownerId);
        return ResponseEntity.ok(bookings);
    }

    // Advanced search with multiple criteria
    @PostMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDTO>> searchBookings(@RequestBody BookingSearchDTO searchDTO) {
        List<BookingResponseDTO> bookings = bookingService.searchBookings(searchDTO);
        return ResponseEntity.ok(bookings);
    }

    // Check if slot is available
    @GetMapping("/check-availability")

    public ResponseEntity<Map<String, Boolean>> checkSlotAvailability(
            @RequestParam Long turfId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        boolean isAvailable = bookingService.isSlotAvailable(turfId, date, startTime, endTime);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isAvailable);
        return ResponseEntity.ok(response);
    }

    // Check for duplicate booking
    @GetMapping("/check-duplicate")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> checkDuplicateBooking(
            @RequestParam Long userId,
            @RequestParam Long turfId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        boolean isDuplicate = bookingService.isDuplicateBooking(userId, turfId, date, startTime, endTime);
        Map<String, Boolean> response = new HashMap<>();
        response.put("duplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }

    // Update booking status
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id,
                                                 @RequestParam BookingStatus status) {
        try {
            BookingResponseDTO updatedBooking = bookingService.updateBookingStatus(id, status);
            return ResponseEntity.ok(updatedBooking);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Calculate total price for booking
    @GetMapping("/calculate-price")
    public ResponseEntity<?> calculateTotalPrice(
            @RequestParam Long turfId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
        try {
            BigDecimal totalPrice = bookingService.calculateTotalPrice(turfId, startTime, endTime);
            Map<String, Object> response = new HashMap<>();
            response.put("totalPrice", totalPrice);
            response.put("turfId", turfId);
            response.put("startTime", startTime);
            response.put("endTime", endTime);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Validate booking time
    @GetMapping("/validate-time")
    public ResponseEntity<Map<String, Boolean>> validateBookingTime(
            @RequestParam Long turfId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        boolean isValid = bookingService.validateBookingTime(turfId, startTime, endTime);
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }

    // Get booking statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getBookingStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBookings", bookingService.getTotalBookingsCount());

        // Add count for each booking status
        for (BookingStatus status : BookingStatus.values()) {
            stats.put("total" + status.name() + "Bookings", bookingService.getBookingsCountByStatus(status));
        }

        return ResponseEntity.ok(stats);
    }

    // Get user booking statistics
    @GetMapping("/statistics/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserBookingStatistics(@PathVariable Long userId) {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalBookings", bookingService.getBookingsCountByUser(userId));
            stats.put("upcomingBookings", bookingService.getUpcomingBookingsForUser(userId));
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Get turf booking statistics
    @GetMapping("/statistics/turf/{turfId}")
    @PreAuthorize("hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<?> getTurfBookingStatistics(@PathVariable Long turfId) {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalBookings", bookingService.getBookingsCountByTurf(turfId));
            stats.put("upcomingBookings", bookingService.getUpcomingBookingsForTurf(turfId));
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}