package com.codegen.suntravels.service;

import com.codegen.suntravels.dto.HotelDTO;
import com.codegen.suntravels.exception.ResourceNotFoundException;
import com.codegen.suntravels.model.Hotel;
import com.codegen.suntravels.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<HotelDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
                              .map(this::convertToDTO)
                              .collect(Collectors.toList());
    }

    public HotelDTO getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                                     .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        return convertToDTO(hotel);
    }

    public HotelDTO createHotel(HotelDTO hotelDTO) {
        Hotel hotel = convertToEntity(hotelDTO);
        Hotel savedHotel = hotelRepository.save(hotel);
        return convertToDTO(savedHotel);
    }

    public HotelDTO updateHotel(Long id, HotelDTO hotelDTO) {
        Hotel existingHotel = hotelRepository.findById(id)
                                             .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        existingHotel.setName(hotelDTO.getName());
        existingHotel.setLocation(hotelDTO.getLocation());
        existingHotel.setContactDetails(hotelDTO.getContactDetails());

        Hotel updatedHotel = hotelRepository.save(existingHotel);
        return convertToDTO(updatedHotel);
    }

    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hotel not found with id: " + id);
        }
        hotelRepository.deleteById(id);
    }

    private HotelDTO convertToDTO(Hotel hotel) {
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setId(hotel.getId());
        hotelDTO.setName(hotel.getName());
        hotelDTO.setLocation(hotel.getLocation());
        hotelDTO.setContactDetails(hotel.getContactDetails());
        return hotelDTO;
    }

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