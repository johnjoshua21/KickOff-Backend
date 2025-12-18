package com.turfBooking.repository;

import com.turfBooking.entity.Turf;
import com.turfBooking.entity.User;
import com.turfBooking.enums.SportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TurfRepository extends JpaRepository<Turf, Long> {

    // Find turfs by owner
    List<Turf> findByOwner(User owner);

    // Find turfs by owner ID
    List<Turf> findByOwnerId(Long ownerId);

    // Find turfs by sport type
    List<Turf> findByType(SportType type);

    // Find turfs by location containing (search functionality)
    List<Turf> findByLocationContainingIgnoreCase(String location);

    // Find turfs by name containing (search functionality)
    List<Turf> findByNameContainingIgnoreCase(String name);

    // Find turfs by price range
    List<Turf> findByPricePerSlotBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Find turfs by price less than or equal
    List<Turf> findByPricePerSlotLessThanEqual(BigDecimal maxPrice);

    // Search turfs by multiple criteria
    @Query("SELECT t FROM Turf t WHERE " +
            "(:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:location IS NULL OR LOWER(t.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:type IS NULL OR t.type = :type) AND " +
            "(:minPrice IS NULL OR t.pricePerSlot >= :minPrice) AND " +
            "(:maxPrice IS NULL OR t.pricePerSlot <= :maxPrice)")
    List<Turf> searchTurfs(@Param("name") String name,
                           @Param("location") String location,
                           @Param("type") SportType type,
                           @Param("minPrice") BigDecimal minPrice,
                           @Param("maxPrice") BigDecimal maxPrice);

    // Count turfs by sport type
    long countByType(SportType type);

    // Count turfs by owner
    long countByOwner(User owner);

    // Find turfs with bookings count
    @Query("SELECT t FROM Turf t LEFT JOIN t.bookings b GROUP BY t.id ORDER BY COUNT(b) DESC")
    List<Turf> findTurfsOrderedByBookingCount();

    // Check if turf name exists for owner (to prevent duplicate names per owner)
    boolean existsByNameAndOwner(String name, User owner);

    // Find available turfs (not having bookings in a specific time slot - to be used with additional logic)
    @Query("SELECT DISTINCT t FROM Turf t WHERE t.id NOT IN " +
            "(SELECT b.turf.id FROM Booking b WHERE b.bookingDate = :date)")
    List<Turf> findAvailableTurfsOnDate(@Param("date") java.time.LocalDate date);
}