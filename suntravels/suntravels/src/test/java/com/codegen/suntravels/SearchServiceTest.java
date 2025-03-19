//package com.codegen.suntravels;
//
//import com.codegen.suntravels.dto.RoomAvailabilityReportDTO;
//import com.codegen.suntravels.dto.RoomRequestDTO;
//import com.codegen.suntravels.dto.SearchRequestDTO;
//import com.codegen.suntravels.dto.SearchResultDTO;
//import com.codegen.suntravels.model.Contract;
//import com.codegen.suntravels.model.Hotel;
//import com.codegen.suntravels.model.RoomType;
//import com.codegen.suntravels.service.SearchService;
//import com.codegen.suntravels.repository.ContractRepository;
//import com.codegen.suntravels.repository.RoomTypeRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class SearchServiceTest {
//
//    @Mock
//    private ContractRepository contractRepository;
//
//    @Mock
//    private RoomTypeRepository roomTypeRepository;
//
//    @InjectMocks
//    private SearchService searchService;
//
//    private Hotel hotel;
//    private Contract contract;
//    private RoomType roomType;
//    private SearchRequestDTO searchRequestDTO;
//    private LocalDate checkInDate;
//    private LocalDate checkOutDate;
//
//    @BeforeEach
//    void setUp() {
//        checkInDate = LocalDate.now().plusDays(1);
//        checkOutDate = checkInDate.plusDays(3);
//
//        // Setup test data
//        hotel = new Hotel();
//        hotel.setId(1L);
//        hotel.setName("Test Hotel");
//        hotel.setLocation("Test Location");
//        hotel.setContactDetails("Test Contact");
//
//        contract = new Contract();
//        contract.setId(1L);
//        contract.setHotel(hotel);
//        contract.setStartDate(LocalDate.now().minusDays(10));
//        contract.setEndDate(LocalDate.now().plusDays(30));
//        contract.setMarkupPercentage(10.0);
//
//        roomType = new RoomType();
//        roomType.setId(1L);
//        roomType.setName("Deluxe Room");
//        roomType.setContract(contract);
//        roomType.setPricePerPerson(100.0);
//        roomType.setNumberOfRooms(5);
//        roomType.setMaxAdults(2);
//
//        contract.setRoomTypes(Collections.singletonList(roomType));
//
//        // Setup search request
//        RoomRequestDTO roomRequestDTO = new RoomRequestDTO(2);
//        searchRequestDTO = new SearchRequestDTO();
//        searchRequestDTO.setCheckInDate(checkInDate);
//        searchRequestDTO.setNumberOfNights(3);
//        searchRequestDTO.setRoomRequests(Collections.singletonList(roomRequestDTO));
//    }
//
//    @Test
//    void searchAvailableRooms_WithValidRequest_ShouldReturnResults() {
//        // Given
//        when(contractRepository.findValidContractsForDateRange(any(LocalDate.class), any(LocalDate.class)))
//                .thenReturn(Collections.singletonList(contract));
//
//        // When
//        List<SearchResultDTO> results = searchService.searchAvailableRooms(searchRequestDTO);
//
//        // Then
//        assertNotNull(results);
//        assertEquals(1, results.size());
//        assertEquals("Test Hotel", results.get(0).getHotelName());
//        assertEquals(1, results.get(0).getAvailableRoomTypes().size());
//        assertTrue(results.get(0).getAvailableRoomTypes().get(0).isAvailable());
//        assertEquals(660.0, results.get(0).getAvailableRoomTypes().get(0).getTotalPrice());
//
//        verify(contractRepository).findValidContractsForDateRange(checkInDate, checkOutDate);
//    }
//
//    @Test
//    void searchAvailableRooms_WithInvalidRequest_ShouldReturnEmptyList() {
//        // Given
//        SearchRequestDTO invalidRequest = new SearchRequestDTO();
//        invalidRequest.setCheckInDate(null);
//        invalidRequest.setNumberOfNights(3);
//        invalidRequest.setRoomRequests(Collections.singletonList(new RoomRequestDTO(2)));
//
//        // When
//        List<SearchResultDTO> results = searchService.searchAvailableRooms(invalidRequest);
//
//        // Then
//        assertNotNull(results);
//        assertTrue(results.isEmpty());
//
//        verify(contractRepository, never()).findValidContractsForDateRange(any(), any());
//    }
//
//    @Test
//    void searchAvailableRooms_WithNoAvailableRooms_ShouldReturnEmptyRoomTypes() {
//        // Given
//        RoomType unavailableRoomType = new RoomType();
//        unavailableRoomType.setId(2L);
//        unavailableRoomType.setName("Small Room");
//        unavailableRoomType.setContract(contract);
//        unavailableRoomType.setPricePerPerson(80.0);
//        unavailableRoomType.setNumberOfRooms(1);
//        unavailableRoomType.setMaxAdults(1);
//
//        contract.setRoomTypes(Collections.singletonList(unavailableRoomType));
//
//        when(contractRepository.findValidContractsForDateRange(any(LocalDate.class), any(LocalDate.class)))
//                .thenReturn(Collections.singletonList(contract));
//
//        // When
//        List<SearchResultDTO> results = searchService.searchAvailableRooms(searchRequestDTO);
//
//        // Then
//        assertNotNull(results);
//        assertEquals(1, results.size());
//        assertEquals("Test Hotel", results.get(0).getHotelName());
//        assertEquals(1, results.get(0).getAvailableRoomTypes().size());
//        assertFalse(results.get(0).getAvailableRoomTypes().get(0).isAvailable());
//        assertEquals(0.0, results.get(0).getAvailableRoomTypes().get(0).getTotalPrice());
//    }
//
//    @Test
//    void searchAvailableRooms_WithNoContracts_ShouldReturnEmptyList() {
//        // Given
//        when(contractRepository.findValidContractsForDateRange(any(LocalDate.class), any(LocalDate.class)))
//                .thenReturn(Collections.emptyList());
//
//        // When
//        List<SearchResultDTO> results = searchService.searchAvailableRooms(searchRequestDTO);
//
//        // Then
//        assertNotNull(results);
//        assertTrue(results.isEmpty());
//    }
//
//    @Test
//    void generateRoomAvailabilityReport_ShouldReturnReport() {
//        // Given
//        LocalDate fromDate = LocalDate.now();
//        LocalDate toDate = LocalDate.now().plusDays(5);
//
//        when(contractRepository.findValidContractsForDateRange(fromDate, toDate))
//                .thenReturn(Collections.singletonList(contract));
//
//        // When
//        List<RoomAvailabilityReportDTO> report = searchService.generateRoomAvailabilityReport(fromDate, toDate);
//
//        // Then
//        assertNotNull(report);
//        assertEquals(1, report.size());
//        assertEquals("Test Hotel", report.get(0).getHotelName());
//        assertEquals("Deluxe Room", report.get(0).getRoomTypeName());
//        assertEquals(5, report.get(0).getTotalRooms());
//        assertEquals(5, report.get(0).getAvailableRooms());
//        assertEquals(fromDate, report.get(0).getDate());
//
//        verify(contractRepository).findValidContractsForDateRange(fromDate, toDate);
//    }
//
//    @Test
//    void searchAvailableRooms_WithMultipleRoomRequests_ShouldCalculateCorrectTotalPrice() {
//        // Given
//        RoomRequestDTO roomRequest1 = new RoomRequestDTO(2);
//        RoomRequestDTO roomRequest2 = new RoomRequestDTO(1);
//
//        searchRequestDTO.setRoomRequests(Arrays.asList(roomRequest1, roomRequest2));
//
//        when(contractRepository.findValidContractsForDateRange(any(LocalDate.class), any(LocalDate.class)))
//                .thenReturn(Collections.singletonList(contract));
//
//        // When
//        List<SearchResultDTO> results = searchService.searchAvailableRooms(searchRequestDTO);
//
//        // Then
//        assertNotNull(results);
//        assertEquals(1, results.size());
//        assertEquals(1, results.get(0).getAvailableRoomTypes().size());
//        assertTrue(results.get(0).getAvailableRoomTypes().get(0).isAvailable());
//
//        // Price calculation: 100 * (1 + 10/100) * 3 nights * (2 adults + 1 adult) = 990
//        assertEquals(990.0, results.get(0).getAvailableRoomTypes().get(0).getTotalPrice());
//    }
//}

package com.codegen.suntravels;

import com.codegen.suntravels.dto.*;
import com.codegen.suntravels.model.Contract;
import com.codegen.suntravels.model.Hotel;
import com.codegen.suntravels.model.RoomType;
import com.codegen.suntravels.service.SearchService;
import com.codegen.suntravels.repository.ContractRepository;
import com.codegen.suntravels.repository.RoomTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private RoomTypeRepository roomTypeRepository;

    @InjectMocks
    private SearchService searchService;

    private Hotel hotel;
    private Contract contract;
    private RoomType roomType;
    private SearchRequestDTO searchRequestDTO;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @BeforeEach
    void setUp() {
        checkInDate = LocalDate.now().plusDays(1);
        checkOutDate = checkInDate.plusDays(3);

        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");

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

        contract.setRoomTypes(Collections.singletonList(roomType));

        RoomRequestDTO roomRequestDTO = new RoomRequestDTO(2);
        searchRequestDTO = new SearchRequestDTO();
        searchRequestDTO.setCheckInDate(checkInDate);
        searchRequestDTO.setNumberOfNights(3);
        searchRequestDTO.setRoomRequests(Collections.singletonList(roomRequestDTO));
    }

    @Test
    void searchAvailableRooms_WithValidRequest_ShouldReturnResults() {
        when(contractRepository.findValidContractsForDateRange(checkInDate, checkOutDate))
                .thenReturn(Collections.singletonList(contract));

        List<SearchResultDTO> results = searchService.searchAvailableRooms(searchRequestDTO);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Test Hotel", results.get(0).getHotelName());
        assertTrue(results.get(0).getAvailableRoomTypes().get(0).isAvailable());
        assertEquals(660.0, results.get(0).getAvailableRoomTypes().get(0).getTotalPrice());

        verify(contractRepository).findValidContractsForDateRange(checkInDate, checkOutDate);
    }

    @Test
    void searchAvailableRooms_WithEmptyRoomRequests_ShouldReturnEmptyList() {
        searchRequestDTO.setRoomRequests(Collections.emptyList());

        List<SearchResultDTO> results = searchService.searchAvailableRooms(searchRequestDTO);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void searchAvailableRooms_WithNegativeNumberOfNights_ShouldReturnEmptyList() {
        searchRequestDTO.setNumberOfNights(-1);

        List<SearchResultDTO> results = searchService.searchAvailableRooms(searchRequestDTO);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void searchAvailableRooms_WithRoomRequestExceedingAvailableRooms_ShouldReturnUnavailable() {
        searchRequestDTO.setRoomRequests(Arrays.asList(new RoomRequestDTO(2), new RoomRequestDTO(2), new RoomRequestDTO(2),
                new RoomRequestDTO(2), new RoomRequestDTO(2), new RoomRequestDTO(2))); // 6 requests, but only 5 rooms

        when(contractRepository.findValidContractsForDateRange(any(), any()))
                .thenReturn(Collections.singletonList(contract));

        List<SearchResultDTO> results = searchService.searchAvailableRooms(searchRequestDTO);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertFalse(results.get(0).getAvailableRoomTypes().get(0).isAvailable());
    }

    @Test
    void searchAvailableRooms_WithHighMarkup_ShouldCalculatePriceCorrectly() {
        contract.setMarkupPercentage(50.0);
        when(contractRepository.findValidContractsForDateRange(any(), any()))
                .thenReturn(Collections.singletonList(contract));

        List<SearchResultDTO> results = searchService.searchAvailableRooms(searchRequestDTO);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(900.0, results.get(0).getAvailableRoomTypes().get(0).getTotalPrice()); // 100 * 1.5 * 3 * 2 = 900
    }

    @Test
    void searchAvailableRooms_WithContractsHavingNoRoomTypes_ShouldReturnEmptyResults() {
        contract.setRoomTypes(Collections.emptyList());
        when(contractRepository.findValidContractsForDateRange(any(), any()))
                .thenReturn(Collections.singletonList(contract));

        List<SearchResultDTO> results = searchService.searchAvailableRooms(searchRequestDTO);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void searchAvailableRooms_WithNullContracts_ShouldReturnEmptyResults() {
        when(contractRepository.findValidContractsForDateRange(any(), any()))
                .thenReturn(null);

        List<SearchResultDTO> results = searchService.searchAvailableRooms(searchRequestDTO);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void searchAvailableRooms_WithZeroRoomPrice_ShouldReturnZeroPrice() {
        roomType.setPricePerPerson(0.0);
        when(contractRepository.findValidContractsForDateRange(any(), any()))
                .thenReturn(Collections.singletonList(contract));

        List<SearchResultDTO> results = searchService.searchAvailableRooms(searchRequestDTO);

        assertNotNull(results);
        assertEquals(0.0, results.get(0).getAvailableRoomTypes().get(0).getTotalPrice());
    }

    @Test
    void generateRoomAvailabilityReport_ShouldReturnReport() {
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now().plusDays(5);

        when(contractRepository.findValidContractsForDateRange(fromDate, toDate))
                .thenReturn(Collections.singletonList(contract));

        List<RoomAvailabilityReportDTO> report = searchService.generateRoomAvailabilityReport(fromDate, toDate);

        assertNotNull(report);
        assertEquals(1, report.size());
        assertEquals("Test Hotel", report.get(0).getHotelName());
        assertEquals("Deluxe Room", report.get(0).getRoomTypeName());
        assertEquals(5, report.get(0).getTotalRooms());
        assertEquals(5, report.get(0).getAvailableRooms());
        assertEquals(fromDate, report.get(0).getDate());

        verify(contractRepository).findValidContractsForDateRange(fromDate, toDate);
    }
}
