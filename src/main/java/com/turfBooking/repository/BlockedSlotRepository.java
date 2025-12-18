package com.turfBooking.repository;

import com.turfBooking.entity.BlockedSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BlockedSlotRepository extends JpaRepository<BlockedSlot, Long> {

    // Find blocked slots by turf ID
    List<BlockedSlot> findByTurfId(Long turfId);

    // Find blocked slots by turf ID and date
    List<BlockedSlot> findByTurfIdAndBlockedDate(Long turfId, LocalDate blockedDate);

    // Find blocked slots by date
    List<BlockedSlot> findByBlockedDate(LocalDate blockedDate);

    // Find blocked slots by date range
    List<BlockedSlot> findByBlockedDateBetween(LocalDate startDate, LocalDate endDate);

    // Check if slot is blocked - find conflicting blocked slots
    @Query("SELECT bs FROM BlockedSlot bs WHERE bs.turf.id = :turfId AND bs.blockedDate = :date " +
            "AND ((bs.startTime < :endTime AND bs.endTime > :startTime))")
    List<BlockedSlot> findConflictingBlockedSlots(@Param("turfId") Long turfId,
                                                  @Param("date") LocalDate date,
                                                  @Param("startTime") LocalTime startTime,
                                                  @Param("endTime") LocalTime endTime);

    // Find blocked slots for turf owner (all their turfs)
    @Query("SELECT bs FROM BlockedSlot bs WHERE bs.turf.owner.id = :ownerId")
    List<BlockedSlot> findBlockedSlotsByTurfOwnerId(@Param("ownerId") Long ownerId);

    // Find future blocked slots for a turf
    @Query("SELECT bs FROM BlockedSlot bs WHERE bs.turf.id = :turfId AND bs.blockedDate >= :currentDate")
    List<BlockedSlot> findFutureBlockedSlotsByTurfId(@Param("turfId") Long turfId, @Param("currentDate") LocalDate currentDate);

    // Delete old blocked slots (cleanup)
    @Query("DELETE FROM BlockedSlot bs WHERE bs.blockedDate < :date")
    void deleteOldBlockedSlots(@Param("date") LocalDate date);
}