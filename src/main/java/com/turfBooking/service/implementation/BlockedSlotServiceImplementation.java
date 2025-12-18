// BlockedSlotServiceImpl.java
package com.turfBooking.service.implementation;

import com.turfBooking.dto.BlockedSlotRequestDTO;
import com.turfBooking.dto.BlockedSlotResponseDTO;
import com.turfBooking.entity.BlockedSlot;
import com.turfBooking.entity.Turf;
import com.turfBooking.repository.BlockedSlotRepository;
import com.turfBooking.repository.TurfRepository;
import com.turfBooking.service.interfaces.BlockedSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BlockedSlotServiceImplementation implements BlockedSlotService {

    @Autowired
    private BlockedSlotRepository blockedSlotRepository;

    @Autowired
    private TurfRepository turfRepository;

    @Override
    public BlockedSlotResponseDTO createBlockedSlot(BlockedSlotRequestDTO requestDTO) {
        // Validate turf exists
        Turf turf = turfRepository.findById(requestDTO.getTurfId())
                .orElseThrow(() -> new RuntimeException("Turf not found with ID: " + requestDTO.getTurfId()));

        // Validate start time is before end time
        if (!requestDTO.getStartTime().isBefore(requestDTO.getEndTime())) {
            throw new RuntimeException("Start time must be before end time");
        }

        // Check if the time slot is within turf operating hours
        if (requestDTO.getStartTime().isBefore(turf.getOperatingStartTime()) ||
                requestDTO.getEndTime().isAfter(turf.getOperatingEndTime())) {
            throw new RuntimeException("Blocked slot must be within turf operating hours");
        }

        // Create blocked slot
        BlockedSlot blockedSlot = new BlockedSlot();
        blockedSlot.setTurf(turf);
        blockedSlot.setBlockedDate(requestDTO.getBlockedDate());
        blockedSlot.setStartTime(requestDTO.getStartTime());
        blockedSlot.setEndTime(requestDTO.getEndTime());

        BlockedSlot savedBlockedSlot = blockedSlotRepository.save(blockedSlot);
        return convertToResponseDTO(savedBlockedSlot);
    }

    @Override
    public List<BlockedSlotResponseDTO> getAllBlockedSlots() {
        return blockedSlotRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BlockedSlotResponseDTO getBlockedSlotById(Long id) {
        BlockedSlot blockedSlot = blockedSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blocked slot not found with ID: " + id));
        return convertToResponseDTO(blockedSlot);
    }

    @Override
    public void deleteBlockedSlot(Long id) {
        if (!blockedSlotRepository.existsById(id)) {
            throw new RuntimeException("Blocked slot not found with ID: " + id);
        }
        blockedSlotRepository.deleteById(id);
    }

    @Override
    public List<BlockedSlotResponseDTO> getBlockedSlotsByTurfId(Long turfId) {
        return blockedSlotRepository.findByTurfId(turfId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlockedSlotResponseDTO> getBlockedSlotsByTurfIdAndDate(Long turfId, LocalDate date) {
        return blockedSlotRepository.findByTurfIdAndBlockedDate(turfId, date)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlockedSlotResponseDTO> getBlockedSlotsByDate(LocalDate date) {
        return blockedSlotRepository.findByBlockedDate(date)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlockedSlotResponseDTO> getBlockedSlotsByDateRange(LocalDate startDate, LocalDate endDate) {
        return blockedSlotRepository.findByBlockedDateBetween(startDate, endDate)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlockedSlotResponseDTO> getBlockedSlotsByTurfOwnerId(Long ownerId) {
        return blockedSlotRepository.findBlockedSlotsByTurfOwnerId(ownerId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlockedSlotResponseDTO> getFutureBlockedSlotsByTurfId(Long turfId) {
        return blockedSlotRepository.findFutureBlockedSlotsByTurfId(turfId, LocalDate.now())
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void cleanupOldBlockedSlots(LocalDate beforeDate) {
        blockedSlotRepository.deleteOldBlockedSlots(beforeDate);
    }

    // Helper method to convert BlockedSlot entity to BlockedSlotResponseDTO
    private BlockedSlotResponseDTO convertToResponseDTO(BlockedSlot blockedSlot) {
        BlockedSlotResponseDTO dto = new BlockedSlotResponseDTO();

        dto.setId(blockedSlot.getId());
        dto.setBlockedDate(blockedSlot.getBlockedDate());
        dto.setStartTime(blockedSlot.getStartTime());
        dto.setEndTime(blockedSlot.getEndTime());

        // Turf details
        Turf turf = blockedSlot.getTurf();
        dto.setTurfId(turf.getId());
        dto.setTurfName(turf.getName());
        dto.setTurfLocation(turf.getLocation());

        return dto;
    }
}