package com.turfBooking.controller;

import com.turfBooking.dto.UserRequestDTO;
import com.turfBooking.dto.UserResponseDTO;
import com.turfBooking.dto.UserUpdateDTO;
import com.turfBooking.enums.UserRole;
import com.turfBooking.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // Create new user
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        try {
            UserResponseDTO createdUser = userService.createUser(userRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get user by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('TURF_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserResponseDTO user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            UserResponseDTO updatedUser = userService.updateUser(id, userUpdateDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Get user by phone
    @GetMapping("/phone/{phone}")
    public ResponseEntity<?> getUserByPhone(@PathVariable String phone) {
        try {
            UserResponseDTO user = userService.getUserByPhone(phone);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Get users by role
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(@PathVariable UserRole role) {
        List<UserResponseDTO> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    // Search users by name
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDTO>> searchUsersByName(@RequestParam String name) {
        List<UserResponseDTO> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }

    // Get all turf owners
    @GetMapping("/turf-owners")
    public ResponseEntity<List<UserResponseDTO>> getAllTurfOwners() {
        List<UserResponseDTO> turfOwners = userService.getAllTurfOwners();
        return ResponseEntity.ok(turfOwners);
    }

    // Get all regular users
    @GetMapping("/regular-users")
    public ResponseEntity<List<UserResponseDTO>> getAllRegularUsers() {
        List<UserResponseDTO> regularUsers = userService.getAllRegularUsers();
        return ResponseEntity.ok(regularUsers);
    }

    // Check if phone exists
    @GetMapping("/phone-exists/{phone}")
    public ResponseEntity<Map<String, Boolean>> checkPhoneExists(@PathVariable String phone) {
        boolean exists = userService.phoneExists(phone);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // Get statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.getTotalUsersCount());
        stats.put("totalRegularUsers", userService.getUsersCountByRole(UserRole.USER));
        stats.put("totalTurfOwners", userService.getUsersCountByRole(UserRole.TURF_OWNER));
        stats.put("totalAdmins", userService.getUsersCountByRole(UserRole.ADMIN));
        return ResponseEntity.ok(stats);
    }
}
