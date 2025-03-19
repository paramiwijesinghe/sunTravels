package com.codegen.suntravels;

import com.codegen.suntravels.dto.HotelDTO;
import com.codegen.suntravels.exception.ResourceNotFoundException;
import com.codegen.suntravels.model.Hotel;
import com.codegen.suntravels.repository.HotelRepository;
import com.codegen.suntravels.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    private Hotel mockHotel;
    private HotelDTO mockHotelDTO;

    @BeforeEach
    void setUp() {
        // Set up test data
        mockHotel = new Hotel();
        mockHotel.setId(1L);
        mockHotel.setName("Test Hotel");
        mockHotel.setLocation("Test Location");
        mockHotel.setContactDetails("Test Contact");

        mockHotelDTO = new HotelDTO();
        mockHotelDTO.setId(1L);
        mockHotelDTO.setName("Test Hotel");
        mockHotelDTO.setLocation("Test Location");
        mockHotelDTO.setContactDetails("Test Contact");
    }

    @Test
    void getAllHotels_ShouldReturnAllHotels() {
        // Arrange
        Hotel anotherHotel = new Hotel();
        anotherHotel.setId(2L);
        anotherHotel.setName("Another Hotel");
        when(hotelRepository.findAll()).thenReturn(Arrays.asList(mockHotel, anotherHotel));

        // Act
        List<HotelDTO> hotels = hotelService.getAllHotels();

        // Assert
        assertThat(hotels).hasSize(2);
        assertThat(hotels.get(0).getName()).isEqualTo("Test Hotel");
        assertThat(hotels.get(1).getName()).isEqualTo("Another Hotel");
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void getHotelById_WithValidId_ShouldReturnHotel() {
        // Arrange
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(mockHotel));

        // Act
        HotelDTO result = hotelService.getHotelById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Hotel");
        verify(hotelRepository, times(1)).findById(1L);
    }

    @Test
    void getHotelById_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(hotelRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            hotelService.getHotelById(999L);
        });
        verify(hotelRepository, times(1)).findById(999L);
    }

    @Test
    void createHotel_ShouldReturnCreatedHotel() {
        // Arrange
        when(hotelRepository.save(any(Hotel.class))).thenReturn(mockHotel);

        // Act
        HotelDTO result = hotelService.createHotel(mockHotelDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Hotel");
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void updateHotel_WithValidId_ShouldReturnUpdatedHotel() {
        // Arrange
        mockHotelDTO.setName("Updated Hotel");
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(mockHotel));
        when(hotelRepository.save(any(Hotel.class))).thenAnswer(invocation -> {
            Hotel hotel = invocation.getArgument(0);
            hotel.setId(1L);
            return hotel;
        });

        // Act
        HotelDTO result = hotelService.updateHotel(1L, mockHotelDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Updated Hotel");
        verify(hotelRepository, times(1)).findById(1L);
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void updateHotel_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(hotelRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            hotelService.updateHotel(999L, mockHotelDTO);
        });
        verify(hotelRepository, times(1)).findById(999L);
        verify(hotelRepository, never()).save(any(Hotel.class));
    }

    @Test
    void deleteHotel_WithValidId_ShouldDeleteHotel() {
        // Arrange
        when(hotelRepository.existsById(1L)).thenReturn(true);
        doNothing().when(hotelRepository).deleteById(1L);

        // Act
        hotelService.deleteHotel(1L);

        // Assert
        verify(hotelRepository, times(1)).existsById(1L);
        verify(hotelRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteHotel_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(hotelRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            hotelService.deleteHotel(999L);
        });
        verify(hotelRepository, times(1)).existsById(999L);
        verify(hotelRepository, never()).deleteById(anyLong());
    }
}