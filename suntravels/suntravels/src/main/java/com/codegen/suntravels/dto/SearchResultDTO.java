package com.codegen.suntravels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) class for representing the result of a hotel room search.
 * It contains details about the hotel name and the available room types with their respective pricing and availability.
 *
 * <p>This class is used to provide the results of a room search, including the hotel name
 * and a list of available room types for the specified criteria.</p>
 *
 * <p>The class includes Lombok annotations to automatically generate getters, setters,
 * a no-arguments constructor, and an all-arguments constructor.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDTO {

    /**
     * The name of the hotel that the search result is for.
     * Represents the hotel that has available rooms matching the search criteria.
     */
    private String hotelName;

    /**
     * A list of available room types for the hotel.
     * This list contains the details of each room type available for booking,
     * such as room type name, pricing, and availability.
     */
    private List<RoomTypeResultDTO> availableRoomTypes = new ArrayList<>();
}
