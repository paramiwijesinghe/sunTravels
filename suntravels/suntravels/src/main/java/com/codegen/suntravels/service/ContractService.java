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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository, HotelRepository hotelRepository) {
        this.contractRepository = contractRepository;
        this.hotelRepository = hotelRepository;
    }

    public List<ContractDTO> getAllContracts() {
        return contractRepository.findAll().stream()
                                 .map(this::convertToDTO)
                                 .collect(Collectors.toList());
    }

    public ContractDTO getContractById(Long id) {
        Contract contract = contractRepository.findById(id)
                                              .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + id));
        return convertToDTO(contract);
    }

    public ContractDTO createContract(ContractDTO contractDTO) {
        Contract contract = convertToEntity(contractDTO);
        Contract savedContract = contractRepository.save(contract);
        return convertToDTO(savedContract);
    }

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

    public void deleteContract(Long id) {
        if (!contractRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contract not found with id: " + id);
        }
        contractRepository.deleteById(id);
    }

    public List<ContractExpiryReportDTO> getContractsExpiringBetween(LocalDate fromDate, LocalDate toDate) {
        List<Contract> expiringContracts = contractRepository.findContractsExpiringBetween(fromDate, toDate);

        return expiringContracts.stream()
                                .map(contract -> {
                                    ContractExpiryReportDTO reportDTO = new ContractExpiryReportDTO();
                                    reportDTO.setContractId(contract.getId());
                                    reportDTO.setHotelName(contract.getHotel().getName());
                                    reportDTO.setStartDate(contract.getStartDate());
                                    reportDTO.setEndDate(contract.getEndDate());

                                    long daysToExpiry = ChronoUnit.DAYS.between(LocalDate.now(), contract.getEndDate());
                                    reportDTO.setDaysToExpiry(Long.valueOf(daysToExpiry));

                                    return reportDTO;
                                })
                                .collect(Collectors.toList());
    }

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


    public List<ContractDTO> searchContracts(String hotelName, LocalDate startDate, LocalDate endDate) {
        List<Contract> contracts = contractRepository.findAll();

        return contracts.stream()
                        .filter(contract -> {
                            boolean matches = true;

                            if (hotelName != null && !hotelName.isEmpty()) {
                                matches = contract.getHotel().getName().toLowerCase().contains(hotelName.toLowerCase());
                            }

//                            if (matches && startDate != null) {
//                                matches = contract.getStartDate().isEqual(startDate) || contract.getStartDate().isAfter(startDate);
//                            }
//
//                            if (matches && endDate != null) {
//                                matches = contract.getEndDate().isEqual(endDate) || contract.getEndDate().isBefore(endDate);
//                            }
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
