package com.codegen.suntravels;

import com.codegen.suntravels.dto.ContractDTO;
import com.codegen.suntravels.dto.ContractExpiryReportDTO;
import com.codegen.suntravels.exception.ResourceNotFoundException;
import com.codegen.suntravels.model.Contract;
import com.codegen.suntravels.model.Hotel;
import com.codegen.suntravels.model.RoomType;
import com.codegen.suntravels.service.ContractService;
import com.codegen.suntravels.repository.ContractRepository;
import com.codegen.suntravels.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private ContractService contractService;

    private Hotel hotel;
    private Contract contract;
    private ContractDTO contractDTO;
    private RoomType roomType;

    @BeforeEach
    void setUp() {
        // Setup test data
        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setContactDetails("Test Contact");

        contract = new Contract();
        contract.setId(1L);
        contract.setHotel(hotel);
        contract.setStartDate(LocalDate.now().minusDays(10));
        contract.setEndDate(LocalDate.now().plusDays(30));
        contract.setMarkupPercentage(10.0);

        roomType = new RoomType();
        roomType.setId(1L);
        roomType.setName("Deluxe Room");
        roomType.setContract(contract);
        roomType.setPricePerPerson(100.0);
        roomType.setNumberOfRooms(5);
        roomType.setMaxAdults(2);

        List<RoomType> roomTypes = new ArrayList<>();
        roomTypes.add(roomType);
        contract.setRoomTypes(roomTypes);

        contractDTO = new ContractDTO();
        contractDTO.setId(1L);
        contractDTO.setHotelId(1L);
        contractDTO.setHotelName("Test Hotel");
        contractDTO.setStartDate(LocalDate.now().minusDays(10));
        contractDTO.setEndDate(LocalDate.now().plusDays(30));
        contractDTO.setMarkupPercentage(10.0);
    }

    @Test
    void getAllContracts_ShouldReturnAllContracts() {
        // Given
        when(contractRepository.findAll()).thenReturn(Collections.singletonList(contract));

        // When
        List<ContractDTO> result = contractService.getAllContracts();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(contract.getId(), result.get(0).getId());
        assertEquals(hotel.getId(), result.get(0).getHotelId());
        assertEquals(hotel.getName(), result.get(0).getHotelName());
        assertEquals(contract.getStartDate(), result.get(0).getStartDate());
        assertEquals(contract.getEndDate(), result.get(0).getEndDate());
        assertEquals(contract.getMarkupPercentage(), result.get(0).getMarkupPercentage());

        verify(contractRepository).findAll();
    }

    @Test
    void getContractById_WithValidId_ShouldReturnContract() {
        // Given
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));

        // When
        ContractDTO result = contractService.getContractById(1L);

        // Then
        assertNotNull(result);
        assertEquals(contract.getId(), result.getId());
        assertEquals(hotel.getId(), result.getHotelId());
        assertEquals(hotel.getName(), result.getHotelName());

        verify(contractRepository).findById(1L);
    }

    @Test
    void getContractById_WithInvalidId_ShouldThrowException() {
        // Given
        when(contractRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> contractService.getContractById(99L));
        verify(contractRepository).findById(99L);
    }

    @Test
    void createContract_ShouldReturnCreatedContract() {
        // Given
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);

        // When
        ContractDTO result = contractService.createContract(contractDTO);

        // Then
        assertNotNull(result);
        assertEquals(contract.getId(), result.getId());
        assertEquals(hotel.getId(), result.getHotelId());
        assertEquals(hotel.getName(), result.getHotelName());

        verify(hotelRepository).findById(1L);
        verify(contractRepository).save(any(Contract.class));
    }

    @Test
    void updateContract_WithValidId_ShouldReturnUpdatedContract() {
        // Given
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);

        // When
        ContractDTO result = contractService.updateContract(1L, contractDTO);

        // Then
        assertNotNull(result);
        assertEquals(contract.getId(), result.getId());
        assertEquals(hotel.getId(), result.getHotelId());
        assertEquals(hotel.getName(), result.getHotelName());

        verify(contractRepository).findById(1L);
        verify(hotelRepository).findById(1L);
        verify(contractRepository).save(any(Contract.class));
    }

    @Test
    void updateContract_WithInvalidId_ShouldThrowException() {
        // Given
        when(contractRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> contractService.updateContract(99L, contractDTO));
        verify(contractRepository).findById(99L);
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    void deleteContract_WithValidId_ShouldDeleteContract() {
        // Given
        when(contractRepository.existsById(1L)).thenReturn(true);
        doNothing().when(contractRepository).deleteById(1L);

        // When
        contractService.deleteContract(1L);

        // Then
        verify(contractRepository).existsById(1L);
        verify(contractRepository).deleteById(1L);
    }

    @Test
    void deleteContract_WithInvalidId_ShouldThrowException() {
        // Given
        when(contractRepository.existsById(99L)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> contractService.deleteContract(99L));
        verify(contractRepository).existsById(99L);
        verify(contractRepository, never()).deleteById(any());
    }

    @Test
    void getContractsExpiringBetween_ShouldReturnExpiringContracts() {
        // Given
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now().plusDays(30);

        when(contractRepository.findContractsExpiringBetween(fromDate, toDate))
                .thenReturn(Collections.singletonList(contract));

        // When
        List<ContractExpiryReportDTO> result = contractService.getContractsExpiringBetween(fromDate, toDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(contract.getId(), result.get(0).getContractId());
        assertEquals(hotel.getName(), result.get(0).getHotelName());
        assertEquals(contract.getStartDate(), result.get(0).getStartDate());
        assertEquals(contract.getEndDate(), result.get(0).getEndDate());

        verify(contractRepository).findContractsExpiringBetween(fromDate, toDate);
    }

    @Test
    void searchContracts_WithHotelName_ShouldReturnMatchingContracts() {
        // Given
        String hotelName = "Test";
        LocalDate startDate = null;
        LocalDate endDate = null;

        when(contractRepository.findAll()).thenReturn(Collections.singletonList(contract));

        // When
        List<ContractDTO> result = contractService.searchContracts(hotelName, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(contract.getId(), result.get(0).getId());
        assertEquals(hotel.getName(), result.get(0).getHotelName());

        verify(contractRepository).findAll();
    }

    @Test
    void searchContracts_WithDateRange_ShouldReturnMatchingContracts() {
        // Given
        String hotelName = null;
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now().plusDays(30);

        when(contractRepository.findAll()).thenReturn(Collections.singletonList(contract));

        // When
        List<ContractDTO> result = contractService.searchContracts(hotelName, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(contract.getId(), result.get(0).getId());

        verify(contractRepository).findAll();
    }

    @Test
    void searchContracts_WithNoMatchingContracts_ShouldReturnEmptyList() {
        // Given
        String hotelName = "NonExistent";
        LocalDate startDate = null;
        LocalDate endDate = null;

        when(contractRepository.findAll()).thenReturn(Collections.singletonList(contract));

        // When
        List<ContractDTO> result = contractService.searchContracts(hotelName, startDate, endDate);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(contractRepository).findAll();
    }
}