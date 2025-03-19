package com.codegen.suntravels.service;

import com.codegen.suntravels.dto.ContractDTO;
import com.codegen.suntravels.dto.ContractExpiryReportDTO;
import com.codegen.suntravels.dto.RoomTypeDTO;
import com.codegen.suntravels.exception.ResourceNotFoundException;
import com.codegen.suntravels.model.Contract;
import com.codegen.suntravels.model.Hotel;
import com.codegen.suntravels.model.RoomType;
import com.codegen.suntravels.repository.ContractRepository;
import com.codegen.suntravels.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling business logic related to contracts.
 * This service provides methods for creating, updating, deleting, retrieving,
 * and searching contracts, as well as generating reports on contracts expiring
 * within a specific date range.
 */
@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository, HotelRepository hotelRepository) {
        this.contractRepository = contractRepository;
        this.hotelRepository = hotelRepository;
    }

    /**
     * Retrieves all contracts and converts them to DTOs.
     *
     * @return a list of {@link ContractDTO} representing all contracts
     */
    public List<ContractDTO> getAllContracts() {
        return contractRepository.findAll().stream()
                                 .map(this::convertToDTO)
                                 .collect(Collectors.toList());
    }

    /**
     * Retrieves a contract by its ID and converts it to a DTO.
     *
     * @param id the unique identifier of the contract
     * @return the {@link ContractDTO} representing the contract
     * @throws ResourceNotFoundException if the contract with the specified ID is not found
     */
    public ContractDTO getContractById(Long id) {
        Contract contract = contractRepository.findById(id)
                                              .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + id));
        return convertToDTO(contract);
    }

    /**
     * Creates a new contract based on the provided {@link ContractDTO}.
     *
     * @param contractDTO the DTO containing contract details
     * @return the created {@link ContractDTO}
     */
    public ContractDTO createContract(ContractDTO contractDTO) {
        Contract contract = convertToEntity(contractDTO);
        Contract savedContract = contractRepository.save(contract);
        return convertToDTO(savedContract);
    }

    /**
     * Updates an existing contract by its ID with the details from the provided {@link ContractDTO}.
     *
     * @param id the unique identifier of the contract to update
     * @param contractDTO the DTO containing updated contract details
     * @return the updated {@link ContractDTO}
     * @throws ResourceNotFoundException if the contract with the specified ID is not found
     */
    public ContractDTO updateContract(Long id, ContractDTO contractDTO) {
        Contract existingContract = contractRepository.findById(id)
                                                      .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + id));

        Hotel hotel = hotelRepository.findById(contractDTO.getHotelId())
                                     .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + contractDTO.getHotelId()));

        existingContract.setHotel(hotel);
        existingContract.setStartDate(contractDTO.getStartDate());
        existingContract.setEndDate(contractDTO.getEndDate());
        existingContract.setMarkupPercentage(contractDTO.getMarkupPercentage());

        Contract updatedContract = contractRepository.save(existingContract);
        return convertToDTO(updatedContract);
    }

    /**
     * Deletes a contract by its ID.
     *
     * @param id the unique identifier of the contract to delete
     * @throws ResourceNotFoundException if the contract with the specified ID is not found
     */
    public void deleteContract(Long id) {
        if (!contractRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contract not found with id: " + id);
        }
        contractRepository.deleteById(id);
    }

    /**
     * Retrieves contracts that are expiring between the specified dates and generates an expiry report.
     *
     * @param fromDate the start date of the expiry range
     * @param toDate the end date of the expiry range
     * @return a list of {@link ContractExpiryReportDTO} containing contract expiry details
     */
    public List<ContractExpiryReportDTO> getContractsExpiringBetween(LocalDate fromDate, LocalDate toDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Contract> expiringContractsPage = contractRepository.findContractsExpiringBetween(fromDate, toDate, pageable);

        return expiringContractsPage.stream()
                                    .map(contract -> {
                                        ContractExpiryReportDTO reportDTO = new ContractExpiryReportDTO();
                                        reportDTO.setContractId(contract.getId());
                                        reportDTO.setHotelName(contract.getHotel().getName());
                                        reportDTO.setStartDate(contract.getStartDate());
                                        reportDTO.setEndDate(contract.getEndDate());

                                        long daysToExpiry = ChronoUnit.DAYS.between(LocalDate.now(), contract.getEndDate());
                                        reportDTO.setDaysToExpiry(daysToExpiry);

                                        return reportDTO;
                                    })
                                    .collect(Collectors.toList());
    }


    /**
     * Converts a {@link Contract} entity to a {@link ContractDTO}.
     *
     * @param contract the contract entity to convert
     * @return the converted {@link ContractDTO}
     */
    private ContractDTO convertToDTO(Contract contract) {
        ContractDTO contractDTO = new ContractDTO();
        contractDTO.setId(contract.getId());
        contractDTO.setHotelId(contract.getHotel().getId());
        contractDTO.setHotelName(contract.getHotel().getName());
        contractDTO.setStartDate(contract.getStartDate());
        contractDTO.setEndDate(contract.getEndDate());
        contractDTO.setMarkupPercentage(contract.getMarkupPercentage());

        List<RoomTypeDTO> roomTypeDTOs = contract.getRoomTypes().stream()
                                                 .map(this::convertRoomTypeToDTO)
                                                 .collect(Collectors.toList());

        contractDTO.setRoomTypes(roomTypeDTOs);

        return contractDTO;
    }

    /**
     * Converts a {@link RoomType} entity to a {@link RoomTypeDTO}.
     *
     * @param roomType the room type entity to convert
     * @return the converted {@link RoomTypeDTO}
     */
    private RoomTypeDTO convertRoomTypeToDTO(RoomType roomType) {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setId(roomType.getId());
        roomTypeDTO.setName(roomType.getName());
        roomTypeDTO.setContractId(roomType.getContract().getId());
        roomTypeDTO.setPricePerPerson(roomType.getPricePerPerson());
        roomTypeDTO.setNumberOfRooms(roomType.getNumberOfRooms());
        roomTypeDTO.setMaxAdults(roomType.getMaxAdults());
        return roomTypeDTO;
    }

    /**
     * Converts a {@link ContractDTO} to a {@link Contract} entity.
     *
     * @param contractDTO the contract DTO to convert
     * @return the converted {@link Contract} entity
     */
    private Contract convertToEntity(ContractDTO contractDTO) {
        Contract contract = new Contract();
        contract.setId(contractDTO.getId());

        if (contractDTO.getHotelId() != null) {
            Hotel hotel = hotelRepository.findById(contractDTO.getHotelId())
                                         .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + contractDTO.getHotelId()));
            contract.setHotel(hotel);
        }

        contract.setStartDate(contractDTO.getStartDate());
        contract.setEndDate(contractDTO.getEndDate());
        contract.setMarkupPercentage(contractDTO.getMarkupPercentage());

        return contract;
    }

    /**
     * Searches for contracts based on hotel name and date range.
     *
     * @param hotelName the hotel name to search for
     * @param startDate the start date of the search range
     * @param endDate the end date of the search range
     * @return a list of {@link ContractDTO} matching the search criteria
     */
    public List<ContractDTO> searchContracts(String hotelName, LocalDate startDate, LocalDate endDate) {
        List<Contract> contracts = contractRepository.findAll();

        return contracts.stream()
                        .filter(contract -> {
                            boolean matches = true;

                            if (hotelName != null && !hotelName.isEmpty()) {
                                matches = contract.getHotel().getName().toLowerCase().contains(hotelName.toLowerCase());
                            }

                            if (matches && startDate != null && endDate != null) {
                                // Contract must start on or before startDate AND end on or after endDate
                                matches = !contract.getStartDate().isAfter(startDate) &&
                                                  !contract.getEndDate().isBefore(endDate);
                            }

                            return matches;
                        })
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
    }
}
