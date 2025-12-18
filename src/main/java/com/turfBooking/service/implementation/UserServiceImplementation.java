package com.turfBooking.service.implementation;

import com.turfBooking.dto.UserRequestDTO;
import com.turfBooking.dto.UserResponseDTO;
import com.turfBooking.dto.UserUpdateDTO;
import com.turfBooking.entity.User;
import com.turfBooking.enums.UserRole;
import com.turfBooking.repository.UserRepository;
import com.turfBooking.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    // ADD THIS FOR JWT AUTHENTICATION
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        // Check if phone already exists
        if (userRepository.existsByPhone(userRequestDTO.getPhone())) {
            throw new RuntimeException("Phone number already exists");
        }

        // Create new user entity
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setPhone(userRequestDTO.getPhone());
        // ENCRYPT PASSWORD FOR SECURITY
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setRole(userRequestDTO.getRole());

        // Save user
        User savedUser = userRepository.save(user);

        return convertToResponseDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return convertToDetailedResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update only non-null fields
        if (userUpdateDTO.getName() != null && !userUpdateDTO.getName().trim().isEmpty()) {
            user.setName(userUpdateDTO.getName());
        }

        if (userUpdateDTO.getPhone() != null && !userUpdateDTO.getPhone().trim().isEmpty()) {
            // Check if new phone already exists for another user
            if (!user.getPhone().equals(userUpdateDTO.getPhone()) &&
                    userRepository.existsByPhone(userUpdateDTO.getPhone())) {
                throw new RuntimeException("Phone number already exists");
            }
            user.setPhone(userUpdateDTO.getPhone());
        }

        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().trim().isEmpty()) {
            // ENCRYPT NEW PASSWORD
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return convertToDetailedResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByPhone(String phone) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found with phone: " + phone));

        return convertToDetailedResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> searchUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllTurfOwners() {
        return userRepository.findAllTurfOwners()
                .stream()
                .map(this::convertToDetailedResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllRegularUsers() {
        return userRepository.findAllUsers()
                .stream()
                .map(this::convertToDetailedResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean phoneExists(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalUsersCount() {
        return userRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getUsersCountByRole(UserRole role) {
        return userRepository.countByRole(role);
    }

    // JWT AUTHENTICATION SPECIFIC METHODS

    /**
     * Create user with encrypted password - used by AuthController
     */
    public User createUserEntity(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Check if phone exists - used by AuthController
     */
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    /**
     * Find user by phone - used by CustomUserDetailsService
     */
    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    // Helper method to convert User entity to basic UserResponseDTO
    private UserResponseDTO convertToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getRole()
        );
    }

    // Helper method to convert User entity to detailed UserResponseDTO with counts
    private UserResponseDTO convertToDetailedResponseDTO(User user) {
        UserResponseDTO responseDTO = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getRole()
        );

        // Set counts (handle null collections)
        responseDTO.setTotalBookings(user.getBookings() != null ? user.getBookings().size() : 0);
        responseDTO.setTotalTurfs(user.getTurfs() != null ? user.getTurfs().size() : 0);

        return responseDTO;
    }
}