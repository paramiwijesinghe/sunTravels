package com.codegen.suntravels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a hotel.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDTO {
    /**
     * Unique identifier for the hotel.
     */
    private Long id;

    /**
     * Name of the hotel.
     */
    private String name;

    /**
     * Location of the hotel.
     */
    private String location;

    /**
     * Contact details of the hotel.
     */
    private String contactDetails;
}
