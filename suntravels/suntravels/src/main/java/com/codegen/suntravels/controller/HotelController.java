package com.codegen.suntravels.controller;


import com.codegen.suntravels.dto.HotelDTO;
import com.codegen.suntravels.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing hotels.
 */
@RestController
@RequestMapping("/api/hotels")
@CrossOrigin(origins = "*")
public class HotelController {

    private final HotelService hotelService;

    /**
     * Constructor to initialize HotelController with HotelService.
     *
     * @param hotelService the hotel service to handle business logic
     */
    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    /**
     * Retrieves all hotels.
     *
     * @return a list of all hotels
     */
    @GetMapping("/getallhotels")
    public ResponseEntity<List<HotelDTO>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    /**
     * Retrieves a hotel by its ID.
     *
     * @param id the hotel ID
     * @return the hotel details
     */
    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    /**
     * Creates a new hotel.
     *
     * @param hotelDTO the hotel data transfer object
     * @return the created hotel with HTTP status 201 (CREATED)
     */
    @PostMapping("/createhotel")
    public ResponseEntity<HotelDTO> createHotel(@Valid @RequestBody HotelDTO hotelDTO) {
        return new ResponseEntity<>(hotelService.createHotel(hotelDTO), HttpStatus.CREATED);
    }

    /**
     * Updates an existing hotel.
     *
     * @param id       the hotel ID
     * @param hotelDTO the updated hotel data
     * @return the updated hotel
     */
    @PutMapping("/updatehotel/{id}")
    public ResponseEntity<HotelDTO> updateHotel(@PathVariable Long id, @Valid @RequestBody HotelDTO hotelDTO) {
        return ResponseEntity.ok(hotelService.updateHotel(id, hotelDTO));
    }

    /**
     * Deletes a hotel by its ID.
     *
     * @param id the hotel ID
     * @return a response entity with no content
     */
    @DeleteMapping("deletehotel/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }
}
