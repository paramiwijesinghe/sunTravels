package com.codegen.suntravels.service;

import com.codegen.suntravels.dto.HotelDTO;
import com.codegen.suntravels.exception.ResourceNotFoundException;
import com.codegen.suntravels.model.Hotel;
import com.codegen.suntravels.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling business logic related to hotels.
 * This service provides methods for creating, updating, deleting, retrieving hotels.
 */
@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    /**
     * Retrieves all hotels and converts them to DTOs.
     *
     * @return a list of {@link HotelDTO} representing all hotels
     */
    public List<HotelDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
                              .map(this::convertToDTO)
                              .collect(Collectors.toList());
    }

    /**
     * Retrieves a hotel by its ID and converts it to a DTO.
     *
     * @param id the unique identifier of the hotel
     * @return the {@link HotelDTO} representing the hotel
     * @throws ResourceNotFoundException if the hotel with the specified ID is not found
     */
    public HotelDTO getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                                     .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        return convertToDTO(hotel);
    }

    /**
     * Creates a new hotel based on the provided {@link HotelDTO}.
     *
     * @param hotelDTO the DTO containing hotel details
     * @return the created {@link HotelDTO}
     */
    public HotelDTO createHotel(HotelDTO hotelDTO) {
        Hotel hotel = convertToEntity(hotelDTO);
        Hotel savedHotel = hotelRepository.save(hotel);
        return convertToDTO(savedHotel);
    }

    /**
     * Updates an existing hotel by its ID with the details from the provided {@link HotelDTO}.
     *
     * @param id the unique identifier of the hotel to update
     * @param hotelDTO the DTO containing updated hotel details
     * @return the updated {@link HotelDTO}
     * @throws ResourceNotFoundException if the hotel with the specified ID is not found
     */
    public HotelDTO updateHotel(Long id, HotelDTO hotelDTO) {
        Hotel existingHotel = hotelRepository.findById(id)
                                             .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        existingHotel.setName(hotelDTO.getName());
        existingHotel.setLocation(hotelDTO.getLocation());
        existingHotel.setContactDetails(hotelDTO.getContactDetails());

        Hotel updatedHotel = hotelRepository.save(existingHotel);
        return convertToDTO(updatedHotel);
    }

    /**
     * Deletes a hotel by its ID.
     *
     * @param id the unique identifier of the hotel to delete
     * @throws ResourceNotFoundException if the hotel with the specified ID is not found
     */
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hotel not found with id: " + id);
        }
        hotelRepository.deleteById(id);
    }

    /**
     * Converts a {@link Hotel} entity to a {@link HotelDTO}.
     *
     * @param hotel the hotel entity to convert
     * @return the converted {@link HotelDTO}
     */
    private HotelDTO convertToDTO(Hotel hotel) {
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setId(hotel.getId());
        hotelDTO.setName(hotel.getName());
        hotelDTO.setLocation(hotel.getLocation());
        hotelDTO.setContactDetails(hotel.getContactDetails());
        return hotelDTO;
    }

    /**
     * Converts a {@link HotelDTO} to a {@link Hotel} entity.
     *
     * @param hotelDTO the hotel DTO to convert
     * @return the converted {@link Hotel} entity
     */
    private Hotel convertToEntity(HotelDTO hotelDTO) {
        Hotel hotel = new Hotel();
        // Only set ID if it's not null
        if (hotelDTO.getId() != null) {
            hotel.setId(hotelDTO.getId());
        }
        hotel.setName(hotelDTO.getName());
        hotel.setLocation(hotelDTO.getLocation());
        hotel.setContactDetails(hotelDTO.getContactDetails());
        return hotel;
    }
}
