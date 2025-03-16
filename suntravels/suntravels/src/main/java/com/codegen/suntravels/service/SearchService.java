package com.codegen.suntravels.service;

import com.codegen.suntravels.dto.*;
import com.codegen.suntravels.model.Contract;
import com.codegen.suntravels.model.RoomType;
import com.codegen.suntravels.dto.RoomRequestDTO;
import com.codegen.suntravels.repository.ContractRepository;
import com.codegen.suntravels.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final ContractRepository contractRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Autowired
    public SearchService(ContractRepository contractRepository, RoomTypeRepository roomTypeRepository) {
        this.contractRepository = contractRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    public List<SearchResultDTO> searchAvailableRooms(SearchRequestDTO searchRequestDTO) {
        // Validate input
        if (searchRequestDTO.getCheckInDate() == null ||
                    searchRequestDTO.getNumberOfNights() == null ||
                    searchRequestDTO.getRoomRequests() == null ||
                    searchRequestDTO.getRoomRequests().isEmpty()) {
            return Collections.emptyList();
        }

        // Calculate check-out date
        LocalDate checkOutDate = searchRequestDTO.getCheckInDate().plusDays(searchRequestDTO.getNumberOfNights());

        // Find valid contracts for the date range
        List<Contract> validContracts = contractRepository.findValidContractsForDateRange(
                searchRequestDTO.getCheckInDate(), checkOutDate);

        // Process each contract to find available room types
        List<SearchResultDTO> results = new ArrayList<>();
        for (Contract contract : validContracts) {
            SearchResultDTO result = new SearchResultDTO();
            result.setHotelName(contract.getHotel().getName());

            // Find available room types for this contract based on the requirements
            List<RoomTypeResultDTO> availableRoomTypes = findAvailableRoomTypes(
                    contract,
                    searchRequestDTO.getRoomRequests(),
                    searchRequestDTO.getNumberOfNights()
            );

            if (!availableRoomTypes.isEmpty()) {
                result.setAvailableRoomTypes(availableRoomTypes);
                results.add(result);
            }
        }

        return results;
    }

    private List<RoomTypeResultDTO> findAvailableRoomTypes(
            Contract contract,
            List<RoomRequestDTO> roomRequests,
            Integer numberOfNights) {

        // Calculate the maximum number of adults needed for any room
        int maxAdultsNeeded = roomRequests.stream()
                                          .mapToInt(RoomRequestDTO::getNumberOfAdults)
                                          .max()
                                          .orElse(0);

        // Count how many rooms of each adult capacity are needed
        Map<Integer, Long> roomsNeededByAdultCount = roomRequests.stream()
                                                                 .collect(Collectors.groupingBy(RoomRequestDTO::getNumberOfAdults, Collectors.counting()));

        // Get all room types for this contract that can accommodate the max number of adults
        List<RoomType> possibleRoomTypes = contract.getRoomTypes().stream()
                                                   .filter(rt -> rt.getMaxAdults() >= maxAdultsNeeded)
                                                   .collect(Collectors.toList());

        List<RoomTypeResultDTO> results = new ArrayList<>();

        // Check each room type
        for (RoomType roomType : possibleRoomTypes) {
            // Check if we have enough rooms of this type
            boolean isAvailable = roomType.getNumberOfRooms() >= roomRequests.size();

            // Calculate total price for all requested rooms
            double totalPrice = 0;
            if (isAvailable) {
                for (RoomRequestDTO roomRequest : roomRequests) {
                    // Price = basePrice * markup * nights * adults
                    totalPrice += roomType.getPricePerPerson() *
                                          (1 + contract.getMarkupPercentage() / 100) *
                                          numberOfNights *
                                          roomRequest.getNumberOfAdults();


                    // Round totalPrice to two decimal places
                    totalPrice = Math.round(totalPrice * 100.0) / 100.0;
                }


                RoomTypeResultDTO roomTypeResult = new RoomTypeResultDTO();
                roomTypeResult.setId(roomType.getId());
                roomTypeResult.setName(roomType.getName());
                roomTypeResult.setTotalPrice(totalPrice);
                roomTypeResult.setAvailable(isAvailable);
                roomTypeResult.setMaxAdults(roomType.getMaxAdults());
                roomTypeResult.setAvailableRooms(roomType.getNumberOfRooms());

                results.add(roomTypeResult);
            }
        }

        return results;
    }

    public List<RoomAvailabilityReportDTO> generateRoomAvailabilityReport(LocalDate fromDate, LocalDate toDate) {
        List<RoomAvailabilityReportDTO> report = new ArrayList<>();

        // Find valid contracts for the date range
        List<Contract> validContracts = contractRepository.findValidContractsForDateRange(fromDate, toDate);

        for (Contract contract : validContracts) {
            for (RoomType roomType : contract.getRoomTypes()) {
                RoomAvailabilityReportDTO reportItem = new RoomAvailabilityReportDTO();
                reportItem.setHotelName(contract.getHotel().getName());
                reportItem.setRoomTypeName(roomType.getName());
                reportItem.setTotalRooms(roomType.getNumberOfRooms());
                reportItem.setAvailableRooms(roomType.getNumberOfRooms()); // Assuming all rooms are available
                reportItem.setDate(fromDate); // Simplification, would need booking data for accuracy

                report.add(reportItem);
            }
        }

        return report;
    }
}