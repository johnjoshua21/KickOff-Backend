package com.turfBooking.service.interfaces;

import com.turfBooking.dto.UserRequestDTO;
import com.turfBooking.dto.UserResponseDTO;
import com.turfBooking.dto.UserUpdateDTO;
import com.turfBooking.entity.User;
import com.turfBooking.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // EXISTING METHODS - Keep all your current functionality

    // Create new user
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    // Get user by ID
    UserResponseDTO getUserById(Long id);

    // Get all users
    List<UserResponseDTO> getAllUsers();

    // Update user
    UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO);

    // Delete user
    void deleteUser(Long id);

    // Get user by phone
    UserResponseDTO getUserByPhone(String phone);

    // Get users by role
    List<UserResponseDTO> getUsersByRole(UserRole role);

    // Search users by name
    List<UserResponseDTO> searchUsersByName(String name);

    // Get all turf owners
    List<UserResponseDTO> getAllTurfOwners();

    // Get all regular users
    List<UserResponseDTO> getAllRegularUsers();

    // Check if phone exists
    boolean phoneExists(String phone);

    // Get total users count
    long getTotalUsersCount();

    // Get users count by role
    long getUsersCountByRole(UserRole role);

    // NEW JWT AUTHENTICATION METHODS - Add these to your interface

    /**
     * Create user entity with encrypted password - used by AuthController
     */
    User createUserEntity(User user);

    /**
     * Check if phone exists - used by AuthController
     */
    boolean existsByPhone(String phone);

    /**
     * Find user by phone - used by CustomUserDetailsService
     */
    Optional<User> findByPhone(String phone);
}