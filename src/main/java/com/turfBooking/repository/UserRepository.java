package com.turfBooking.repository;

import com.turfBooking.entity.User;
import com.turfBooking.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by phone (unique identifier)
    Optional<User> findByPhone(String phone);

    // Check if phone already exists
    boolean existsByPhone(String phone);

    // Find users by role
    List<User> findByRole(UserRole role);

    // Find users by name containing (search functionality)
    List<User> findByNameContainingIgnoreCase(String name);

    // Find all turf owners
    @Query("SELECT u FROM User u WHERE u.role = 'TURF_OWNER'")
    List<User> findAllTurfOwners();

    // Find all regular users
    @Query("SELECT u FROM User u WHERE u.role = 'USER'")
    List<User> findAllUsers();

    // Count users by role
    long countByRole(UserRole role);

}