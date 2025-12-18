package com.turfBooking.service.interfaces;

import com.turfBooking.dto.BookingRequestDTO;
import com.turfBooking.dto.BookingResponseDTO;
import com.turfBooking.dto.BookingUpdateDTO;
import com.turfBooking.dto.BookingSearchDTO;
import com.turfBooking.enums.BookingStatus;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    // Create new booking
    BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO);

    // Get booking by ID
    BookingResponseDTO getBookingById(Long id);

    // Get all bookings
    List<BookingResponseDTO> getAllBookings();

    // Update booking
    BookingResponseDTO updateBooking(Long id, BookingUpdateDTO bookingUpdateDTO);

    // Cancel booking
    BookingResponseDTO cancelBooking(Long id);

    // Delete booking
    void deleteBooking(Long id);

    // Get bookings by user ID
    List<BookingResponseDTO> getBookingsByUserId(Long userId);

    // Get bookings by turf ID
    List<BookingResponseDTO> getBookingsByTurfId(Long turfId);

    // Get bookings by status
    List<BookingResponseDTO> getBookingsByStatus(BookingStatus status);

    // Get bookings by date
    List<BookingResponseDTO> getBookingsByDate(LocalDate date);

    // Get bookings by date range
    List<BookingResponseDTO> getBookingsByDateRange(LocalDate startDate, LocalDate endDate);

    // Get upcoming bookings for user
    List<BookingResponseDTO> getUpcomingBookingsForUser(Long userId);

    // Get upcoming bookings for turf
    List<BookingResponseDTO> getUpcomingBookingsForTurf(Long turfId);

    // Get bookings for turf owner (all their turfs)
    List<BookingResponseDTO> getBookingsForTurfOwner(Long ownerId);

    // Get upcoming bookings for turf owner
    List<BookingResponseDTO> getUpcomingBookingsForTurfOwner(Long ownerId);

    // Search bookings with multiple criteria
    List<BookingResponseDTO> searchBookings(BookingSearchDTO searchDTO);

    // Check if booking slot is available
    boolean isSlotAvailable(Long turfId, LocalDate date,
                            java.time.LocalTime startTime, java.time.LocalTime endTime);

    // Check for duplicate booking
    boolean isDuplicateBooking(Long userId, Long turfId, LocalDate date,
                               java.time.LocalTime startTime, java.time.LocalTime endTime);

    // Update booking status
    BookingResponseDTO updateBookingStatus(Long id, BookingStatus status);

    // Get total bookings count
    long getTotalBookingsCount();

    // Get bookings count by status
    long getBookingsCountByStatus(BookingStatus status);

    // Get bookings count by user
    long getBookingsCountByUser(Long userId);

    // Get bookings count by turf
    long getBookingsCountByTurf(Long turfId);

    // Get bookings count by date range
    long getBookingsCountByDateRange(LocalDate startDate, LocalDate endDate);

    // Validate booking time
    boolean validateBookingTime(Long turfId, java.time.LocalTime startTime, java.time.LocalTime endTime);

    // Calculate total price for booking
    java.math.BigDecimal calculateTotalPrice(Long turfId, java.time.LocalTime startTime, java.time.LocalTime endTime);
}