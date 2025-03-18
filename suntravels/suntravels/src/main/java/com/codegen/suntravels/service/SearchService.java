package com.codegen.suntravels.service;

import com.codegen.suntravels.dto.*;
import com.codegen.suntravels.model.Contract;
import com.codegen.suntravels.model.RoomType;
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
        if (searchRequestDTO.getCheckInDate() == null ||
                    searchRequestDTO.getNumberOfNights() == null ||
                    searchRequestDTO.getRoomRequests() == null ||
                    searchRequestDTO.getRoomRequests().isEmpty()) {
            return Collections.emptyList();
        }

        LocalDate checkOutDate = searchRequestDTO.getCheckInDate().plusDays(searchRequestDTO.getNumberOfNights());


        List<Contract> validContracts = contractRepository.findValidContractsForDateRange(
                searchRequestDTO.getCheckInDate(), checkOutDate);

        List<SearchResultDTO> results = new ArrayList<>();

        for (Contract contract : validContracts) {
            SearchResultDTO result = new SearchResultDTO();
            result.setHotelName(contract.getHotel().getName());

            List<RoomTypeResultDTO> allRoomTypes = new ArrayList<>();

            for (RoomType roomType : contract.getRoomTypes()) {
                RoomTypeResultDTO roomTypeResult = new RoomTypeResultDTO();
                roomTypeResult.setId(roomType.getId());
                roomTypeResult.setName(roomType.getName());
                roomTypeResult.setMaxAdults(roomType.getMaxAdults());
                roomTypeResult.setAvailableRooms(roomType.getNumberOfRooms());

                // Check if this room type meets the request criteria
                boolean isAvailable = isRoomAvailable(roomType, searchRequestDTO.getRoomRequests());

                roomTypeResult.setAvailable(isAvailable);

                // Calculate total price if available
                if (isAvailable) {
                    double totalPrice = calculateTotalPrice(roomType, searchRequestDTO.getRoomRequests(),
                            searchRequestDTO.getNumberOfNights(), contract.getMarkupPercentage());
                    roomTypeResult.setTotalPrice(totalPrice);
                } else {
                    roomTypeResult.setTotalPrice(0.0); // Set to 0 if not matching user request
                }

                allRoomTypes.add(roomTypeResult);
            }

            result.setAvailableRoomTypes(allRoomTypes);
            results.add(result);
        }



        return results;

    }

    private boolean isRoomAvailable(RoomType roomType, List<RoomRequestDTO> roomRequests) {
        int maxAdultsNeeded = roomRequests.stream()
                                          .mapToInt(RoomRequestDTO::getNumberOfAdults)
                                          .max()
                                          .orElse(0);

        return roomType.getMaxAdults() >= maxAdultsNeeded && roomType.getNumberOfRooms() >= roomRequests.size();
    }

    private double calculateTotalPrice(RoomType roomType, List<RoomRequestDTO> roomRequests,
                                       Integer numberOfNights, double markupPercentage) {
        double totalPrice = 0;
        for (RoomRequestDTO roomRequest : roomRequests) {
            totalPrice += roomType.getPricePerPerson() *
                                  (1 + markupPercentage / 100) *
                                  numberOfNights *
                                  roomRequest.getNumberOfAdults();
            System.out.println( roomRequest.getNumberOfAdults());

        }
        return Math.round(totalPrice * 100.0) / 100.0;
    }

    public List<RoomAvailabilityReportDTO> generateRoomAvailabilityReport(LocalDate fromDate, LocalDate toDate) {
        List<RoomAvailabilityReportDTO> report = new ArrayList<>();

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
