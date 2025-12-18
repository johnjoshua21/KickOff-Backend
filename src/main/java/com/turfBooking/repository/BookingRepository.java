package com.turfBooking.repository;

import com.turfBooking.entity.Booking;
import com.turfBooking.entity.Turf;
import com.turfBooking.entity.User;
import com.turfBooking.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find bookings by user
    List<Booking> findByUser(User user);

    // Find bookings by user ID
    List<Booking> findByUserId(Long userId);

    // Find bookings by turf
    List<Booking> findByTurf(Turf turf);

    // Find bookings by turf ID
    List<Booking> findByTurfId(Long turfId);

    // Find bookings by status
    List<Booking> findByStatus(BookingStatus status);

    // Find bookings by booking date
    List<Booking> findByBookingDate(LocalDate bookingDate);

    // Find bookings by date range
    List<Booking> findByBookingDateBetween(LocalDate startDate, LocalDate endDate);

    // Find bookings by user and status
    List<Booking> findByUserAndStatus(User user, BookingStatus status);

    // Find bookings by turf and status
    List<Booking> findByTurfAndStatus(Turf turf, BookingStatus status);

    // Find bookings by user and date
    List<Booking> findByUserAndBookingDate(User user, LocalDate bookingDate);

    // Find bookings by turf and date
    List<Booking> findByTurfAndBookingDate(Turf turf, LocalDate bookingDate);

    // Check for conflicting bookings (time overlap)
    @Query("SELECT b FROM Booking b WHERE b.turf.id = :turfId AND b.bookingDate = :date AND b.status = 'CONFIRMED' AND " +
            "((:startTime < b.slotEndTime AND :endTime > b.slotStartTime))")
    List<Booking> findConflictingBookings(@Param("turfId") Long turfId,
                                          @Param("date") LocalDate date,
                                          @Param("startTime") LocalTime startTime,
                                          @Param("endTime") LocalTime endTime);

    // Find upcoming bookings for a user
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND " +
            "(b.bookingDate > :currentDate OR (b.bookingDate = :currentDate AND b.slotStartTime > :currentTime)) " +
            "AND b.status = 'CONFIRMED' ORDER BY b.bookingDate ASC, b.slotStartTime ASC")
    List<Booking> findUpcomingBookingsForUser(@Param("userId") Long userId,
                                              @Param("currentDate") LocalDate currentDate,
                                              @Param("currentTime") LocalTime currentTime);

    // Find upcoming bookings for a turf
    @Query("SELECT b FROM Booking b WHERE b.turf.id = :turfId AND " +
            "(b.bookingDate > :currentDate OR (b.bookingDate = :currentDate AND b.slotStartTime > :currentTime)) " +
            "AND b.status = 'CONFIRMED' ORDER BY b.bookingDate ASC, b.slotStartTime ASC")
    List<Booking> findUpcomingBookingsForTurf(@Param("turfId") Long turfId,
                                              @Param("currentDate") LocalDate currentDate,
                                              @Param("currentTime") LocalTime currentTime);

    // Find bookings for turf owner (all turfs owned by the user)
    @Query("SELECT b FROM Booking b WHERE b.turf.owner.id = :ownerId ORDER BY b.bookingDate DESC, b.slotStartTime DESC")
    List<Booking> findBookingsForTurfOwner(@Param("ownerId") Long ownerId);

    // Find upcoming bookings for turf owner
    @Query("SELECT b FROM Booking b WHERE b.turf.owner.id = :ownerId AND " +
            "(b.bookingDate > :currentDate OR (b.bookingDate = :currentDate AND b.slotStartTime > :currentTime)) " +
            "AND b.status = 'CONFIRMED' ORDER BY b.bookingDate ASC, b.slotStartTime ASC")
    List<Booking> findUpcomingBookingsForTurfOwner(@Param("ownerId") Long ownerId,
                                                   @Param("currentDate") LocalDate currentDate,
                                                   @Param("currentTime") LocalTime currentTime);

    // Count bookings by status
    long countByStatus(BookingStatus status);

    // Count bookings by user
    long countByUser(User user);

    // Count bookings by turf
    long countByTurf(Turf turf);

    // Count bookings by date range
    long countByBookingDateBetween(LocalDate startDate, LocalDate endDate);

    // Find bookings by multiple criteria
    @Query("SELECT b FROM Booking b WHERE " +
            "(:userId IS NULL OR b.user.id = :userId) AND " +
            "(:turfId IS NULL OR b.turf.id = :turfId) AND " +
            "(:status IS NULL OR b.status = :status) AND " +
            "(:startDate IS NULL OR b.bookingDate >= :startDate) AND " +
            "(:endDate IS NULL OR b.bookingDate <= :endDate) " +
            "ORDER BY b.bookingDate DESC, b.slotStartTime DESC")
    List<Booking> searchBookings(@Param("userId") Long userId,
                                 @Param("turfId") Long turfId,
                                 @Param("status") BookingStatus status,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate);

    // Check if user has existing booking for same turf and time slot
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.user.id = :userId AND b.turf.id = :turfId AND " +
            "b.bookingDate = :date AND b.slotStartTime = :startTime AND b.slotEndTime = :endTime AND b.status = 'CONFIRMED'")
    boolean existsDuplicateBooking(@Param("userId") Long userId,
                                   @Param("turfId") Long turfId,
                                   @Param("date") LocalDate date,
                                   @Param("startTime") LocalTime startTime,
                                   @Param("endTime") LocalTime endTime);
}