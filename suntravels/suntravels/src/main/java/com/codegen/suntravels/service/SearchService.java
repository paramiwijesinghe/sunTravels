package com.codegen.suntravels.service;

import com.codegen.suntravels.dto.*;
import com.codegen.suntravels.model.Contract;
import com.codegen.suntravels.model.RoomType;
import com.codegen.suntravels.dto.SearchRequestDTO;
import com.codegen.suntravels.repository.ContractRepository;
import com.codegen.suntravels.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling search functionality related to room availability.
 * Provides methods for searching available rooms based on the user's request and generating room availability reports.
 */
@Service
public class SearchService {

    private final ContractRepository contractRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Autowired
    public SearchService(ContractRepository contractRepository, RoomTypeRepository roomTypeRepository) {
        this.contractRepository = contractRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    // Hardcoded allocated rooms (roomTypeId -> allocated count)
    private static final Map<Long, Integer> allocatedRooms = new HashMap<>();
    static {
        allocatedRooms.put(12L, 2); // Example: Room Type ID 1 has 2 rooms allocated
        allocatedRooms.put(11L, 1); // Example: Room Type ID 2 has 1 room allocated
    }

    /**
     * Searches for available rooms with pagination.
     */
    public Page<SearchResultDTO> searchAvailableRooms(SearchRequestDTO searchRequestDTO, Pageable pageable) {
        if (searchRequestDTO.getCheckInDate() == null ||
                    searchRequestDTO.getNumberOfNights() == null ||
                    searchRequestDTO.getRoomRequests() == null ||
                    searchRequestDTO.getRoomRequests().isEmpty()) {
            return Page.empty(); // Return an empty page if criteria are invalid
        }

        LocalDate checkOutDate = searchRequestDTO.getCheckInDate().plusDays(searchRequestDTO.getNumberOfNights());

        // Use repository to get paginated contracts
        Page<Contract> validContracts = contractRepository.findValidContractsForDateRange(searchRequestDTO.getCheckInDate(), checkOutDate, pageable);

        List<SearchResultDTO> results = new ArrayList<>();

        for (Contract contract : validContracts.getContent()) {
            if (contract.getRoomTypes() == null || contract.getRoomTypes().isEmpty()) continue;

            SearchResultDTO result = new SearchResultDTO();
            result.setHotelName(contract.getHotel().getName());

            List<RoomTypeResultDTO> allRoomTypes = new ArrayList<>();

            for (RoomType roomType : contract.getRoomTypes()) {
                int allocated = allocatedRooms.getOrDefault(roomType.getId(), 0);
                int availableRooms = Math.max(roomType.getNumberOfRooms() - allocated, 0);

                RoomTypeResultDTO roomTypeResult = new RoomTypeResultDTO();
                roomTypeResult.setId(roomType.getId());
                roomTypeResult.setName(roomType.getName());
                roomTypeResult.setMaxAdults(roomType.getMaxAdults());
                roomTypeResult.setAvailableRooms(availableRooms);

                // Check availability
                boolean isAvailable = isRoomAvailable(roomType, searchRequestDTO.getRoomRequests(), availableRooms);
                roomTypeResult.setAvailable(isAvailable);

                // Calculate price if available
                if (isAvailable) {
                    double totalPrice = calculateTotalPrice(roomType, searchRequestDTO.getRoomRequests(),
                            searchRequestDTO.getNumberOfNights(), contract.getMarkupPercentage());
                    roomTypeResult.setTotalPrice(totalPrice);
                } else {
                    roomTypeResult.setTotalPrice(0.0);
                }

                allRoomTypes.add(roomTypeResult);
            }

            if (!allRoomTypes.isEmpty()) {
                result.setAvailableRoomTypes(allRoomTypes);
                results.add(result);
            }
        }

        return new PageImpl<>(results, pageable, validContracts.getTotalElements()); // Return paginated results
    }


    /**
     * Checks if a given room type can accommodate the specified room requests, considering allocated rooms.
     */
    private boolean isRoomAvailable(RoomType roomType, List<RoomRequestDTO> roomRequests, int availableRooms) {
        int totalRoomsRequested = roomRequests.stream()
                                              .mapToInt(RoomRequestDTO::getNumberOfRooms)
                                              .sum();

        boolean canAccommodateAdults = roomRequests.stream()
                                                   .allMatch(request -> roomType.getMaxAdults() >= request.getNumberOfAdults());

        boolean canAccommodateRooms = availableRooms >= totalRoomsRequested;

        return canAccommodateAdults && canAccommodateRooms;
    }

    /**
     * Calculates total price based on available rooms, allocated rooms, and markup.
     */
    private double calculateTotalPrice(RoomType roomType, List<RoomRequestDTO> roomRequests,
                                       Integer numberOfNights, double markupPercentage) {
        int allocated = allocatedRooms.getOrDefault(roomType.getId(), 0);
        int availableRooms = Math.max(roomType.getNumberOfRooms() - allocated, 0);

        double totalPrice = 0;
        for (RoomRequestDTO roomRequest : roomRequests) {
            if (roomRequest.getNumberOfRooms() > availableRooms) {
                return 0.0; // Not enough available rooms
            }

            totalPrice += roomType.getPricePerPerson() *
                                  (1 + markupPercentage / 100) *
                                  numberOfNights *
                                  roomRequest.getNumberOfAdults() *
                                  roomRequest.getNumberOfRooms();
        }
        return Math.round(totalPrice * 100.0) / 100.0;
    }

    /**
     * Generates a room availability report considering allocated rooms.
     */
    public Page<RoomAvailabilityReportDTO> generateRoomAvailabilityReport(LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        List<RoomAvailabilityReportDTO> report = new ArrayList<>();

        // Using pagination
        Page<Contract> validContractsPage = contractRepository.findValidContractsForDateRange(fromDate, toDate, pageable);

        for (Contract contract : validContractsPage.getContent()) {
            for (RoomType roomType : contract.getRoomTypes()) {
                int allocated = allocatedRooms.getOrDefault(roomType.getId(), 0);
                int availableRooms = Math.max(roomType.getNumberOfRooms() - allocated, 0);

                RoomAvailabilityReportDTO reportItem = new RoomAvailabilityReportDTO();
                reportItem.setHotelName(contract.getHotel().getName());
                reportItem.setRoomTypeName(roomType.getName());
                reportItem.setTotalRooms(roomType.getNumberOfRooms());
                reportItem.setAvailableRooms(availableRooms);
                reportItem.setDate(fromDate); // Simplified

                report.add(reportItem);
            }
        }

        return new PageImpl<>(report, pageable, validContractsPage.getTotalElements());
    }

}