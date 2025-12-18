package com.turfBooking.service.implementation;

import com.turfBooking.dto.BookingRequestDTO;
import com.turfBooking.dto.BookingResponseDTO;
import com.turfBooking.dto.BookingUpdateDTO;
import com.turfBooking.dto.BookingSearchDTO;
import com.turfBooking.entity.Booking;
import com.turfBooking.entity.Turf;
import com.turfBooking.entity.User;
import com.turfBooking.enums.BookingStatus;
import com.turfBooking.repository.BookingRepository;
import com.turfBooking.repository.TurfRepository;
import com.turfBooking.repository.UserRepository;
import com.turfBooking.service.interfaces.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImplementation implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TurfRepository turfRepository;

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO) {
        // Validate user exists
        User user = userRepository.findById(bookingRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + bookingRequestDTO.getUserId()));

        // Validate turf exists
        Turf turf = turfRepository.findById(bookingRequestDTO.getTurfId())
                .orElseThrow(() -> new RuntimeException("Turf not found with id: " + bookingRequestDTO.getTurfId()));

        // Validate booking time
        if (!validateBookingTime(bookingRequestDTO.getTurfId(),
                bookingRequestDTO.getSlotStartTime(),
                bookingRequestDTO.getSlotEndTime())) {
            throw new RuntimeException("Invalid booking time: outside turf operating hours");
        }

        // Check if slot is available
        if (!isSlotAvailable(bookingRequestDTO.getTurfId(),
                bookingRequestDTO.getBookingDate(),
                bookingRequestDTO.getSlotStartTime(),
                bookingRequestDTO.getSlotEndTime())) {
            throw new RuntimeException("Time slot is not available");
        }

        // Check for duplicate booking
        if (isDuplicateBooking(bookingRequestDTO.getUserId(),
                bookingRequestDTO.getTurfId(),
                bookingRequestDTO.getBookingDate(),
                bookingRequestDTO.getSlotStartTime(),
                bookingRequestDTO.getSlotEndTime())) {
            throw new RuntimeException("Duplicate booking: User already has this exact booking");
        }

        // Validate booking date (should not be in the past)
        if (bookingRequestDTO.getBookingDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot book for past dates");
        }

        // Create new booking entity
        Booking booking = new Booking();
        booking.setSlotStartTime(bookingRequestDTO.getSlotStartTime());
        booking.setSlotEndTime(bookingRequestDTO.getSlotEndTime());
        booking.setBookingDate(bookingRequestDTO.getBookingDate());
        booking.setUser(user);
        booking.setTurf(turf);
        booking.setStatus(bookingRequestDTO.getStatus());

        // Save booking
        Booking savedBooking = bookingRepository.save(booking);

        return convertToResponseDTO(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        return convertToResponseDTO(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDTO updateBooking(Long id, BookingUpdateDTO bookingUpdateDTO) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        // Only allow updates for future bookings or confirmed bookings
        if (booking.getBookingDate().isBefore(LocalDate.now()) &&
                booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Cannot update past or non-confirmed bookings");
        }

        boolean needsAvailabilityCheck = false;

        // Update time slots if provided
        if (bookingUpdateDTO.getSlotStartTime() != null && bookingUpdateDTO.getSlotEndTime() != null) {
            if (!validateBookingTime(booking.getTurf().getId(),
                    bookingUpdateDTO.getSlotStartTime(),
                    bookingUpdateDTO.getSlotEndTime())) {
                throw new RuntimeException("Invalid booking time: outside turf operating hours");
            }
            needsAvailabilityCheck = true;
            booking.setSlotStartTime(bookingUpdateDTO.getSlotStartTime());
            booking.setSlotEndTime(bookingUpdateDTO.getSlotEndTime());
        }

        // Update booking date if provided
        if (bookingUpdateDTO.getBookingDate() != null) {
            if (bookingUpdateDTO.getBookingDate().isBefore(LocalDate.now())) {
                throw new RuntimeException("Cannot update to past dates");
            }
            needsAvailabilityCheck = true;
            booking.setBookingDate(bookingUpdateDTO.getBookingDate());
        }

        // Check availability if time or date changed
        if (needsAvailabilityCheck) {
            // Temporarily exclude current booking from availability check
            Long currentBookingId = booking.getId();
            List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(
                    booking.getTurf().getId(), booking.getBookingDate(),
                    booking.getSlotStartTime(), booking.getSlotEndTime());

            // Remove current booking from conflicts
            conflictingBookings = conflictingBookings.stream()
                    .filter(b -> !b.getId().equals(currentBookingId))
                    .collect(Collectors.toList());

            if (!conflictingBookings.isEmpty()) {
                throw new RuntimeException("Updated time slot is not available");
            }
        }

        // Update status if provided
        if (bookingUpdateDTO.getStatus() != null) {
            booking.setStatus(bookingUpdateDTO.getStatus());
        }

        Booking updatedBooking = bookingRepository.save(booking);
        return convertToResponseDTO(updatedBooking);
    }

    @Override
    public BookingResponseDTO cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        // Only allow cancellation of confirmed bookings
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed bookings can be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking cancelledBooking = bookingRepository.save(booking);
        return convertToResponseDTO(cancelledBooking);
    }



    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByTurfId(Long turfId) {
        return bookingRepository.findByTurfId(turfId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByDate(LocalDate date) {
        return bookingRepository.findByBookingDate(date)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByDateRange(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findByBookingDateBetween(startDate, endDate)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getUpcomingBookingsForUser(Long userId) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        return bookingRepository.findUpcomingBookingsForUser(userId, currentDate, currentTime)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getUpcomingBookingsForTurf(Long turfId) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        return bookingRepository.findUpcomingBookingsForTurf(turfId, currentDate, currentTime)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsForTurfOwner(Long ownerId) {
        return bookingRepository.findBookingsForTurfOwner(ownerId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getUpcomingBookingsForTurfOwner(Long ownerId) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        return bookingRepository.findUpcomingBookingsForTurfOwner(ownerId, currentDate, currentTime)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> searchBookings(BookingSearchDTO searchDTO) {
        return bookingRepository.searchBookings(
                        searchDTO.getUserId(),
                        searchDTO.getTurfId(),
                        searchDTO.getStatus(),
                        searchDTO.getStartDate(),
                        searchDTO.getEndDate()
                ).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSlotAvailable(Long turfId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(
                turfId, date, startTime, endTime);
        return conflictingBookings.isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isDuplicateBooking(Long userId, Long turfId, LocalDate date,
                                      LocalTime startTime, LocalTime endTime) {
        return bookingRepository.existsDuplicateBooking(userId, turfId, date, startTime, endTime);
    }

    @Override
    public BookingResponseDTO updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        booking.setStatus(status);
        Booking updatedBooking = bookingRepository.save(booking);
        return convertToResponseDTO(updatedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalBookingsCount() {
        return bookingRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getBookingsCountByStatus(BookingStatus status) {
        return bookingRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long getBookingsCountByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return bookingRepository.countByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public long getBookingsCountByTurf(Long turfId) {
        Turf turf = turfRepository.findById(turfId)
                .orElseThrow(() -> new RuntimeException("Turf not found with id: " + turfId));
        return bookingRepository.countByTurf(turf);
    }

    @Override
    @Transactional(readOnly = true)
    public long getBookingsCountByDateRange(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.countByBookingDateBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateBookingTime(Long turfId, LocalTime startTime, LocalTime endTime) {
        Turf turf = turfRepository.findById(turfId)
                .orElseThrow(() -> new RuntimeException("Turf not found with id: " + turfId));

        // Check if start time is before end time
        if (!startTime.isBefore(endTime)) {
            return false;
        }

        // Check if booking time is within turf operating hours
        return !startTime.isBefore(turf.getOperatingStartTime()) &&
                !endTime.isAfter(turf.getOperatingEndTime());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalPrice(Long turfId, LocalTime startTime, LocalTime endTime) {
        Turf turf = turfRepository.findById(turfId)
                .orElseThrow(() -> new RuntimeException("Turf not found with id: " + turfId));

        // Calculate duration in hours
        Duration duration = Duration.between(startTime, endTime);
        long hours = duration.toHours();

        // If there are remaining minutes, round up to next hour
        if (duration.toMinutes() % 60 != 0) {
            hours++;
        }

        return turf.getPricePerSlot().multiply(BigDecimal.valueOf(hours));
    }

    // Helper method to convert Booking entity to BookingResponseDTO
    private BookingResponseDTO convertToResponseDTO(Booking booking) {
        BookingResponseDTO responseDTO = new BookingResponseDTO(
                booking.getId(),
                booking.getSlotStartTime(),
                booking.getSlotEndTime(),
                booking.getBookingDate(),
                booking.getStatus(),
                booking.getUser().getId(),
                booking.getUser().getName(),
                booking.getUser().getPhone(),
                booking.getTurf().getId(),
                booking.getTurf().getName(),
                booking.getTurf().getLocation(),
                booking.getTurf().getType().toString(),
                booking.getTurf().getPricePerSlot(),
                booking.getTurf().getOwner().getName(),
                booking.getTurf().getOwner().getPhone()
        );

        // Calculate additional fields
        Duration duration = Duration.between(booking.getSlotStartTime(), booking.getSlotEndTime());
        long hours = duration.toHours();
        if (duration.toMinutes() % 60 != 0) {
            hours++;
        }
        responseDTO.setDurationHours(hours);
        responseDTO.setTotalPrice(booking.getTurf().getPricePerSlot().multiply(BigDecimal.valueOf(hours)));

        return responseDTO;
    }
}


