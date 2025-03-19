package com.codegen.suntravels.dto;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) class for representing a room type.
 * It contains details about a specific room type in a hotel, including the room's
 * identifier, name, associated contract, price, number of rooms, and maximum adult occupancy.
 *
 * <p>This class is used to hold the data related to room types, which can be used for
 * room availability, pricing, and other hotel-related operations.</p>
 *
 * <p>The class includes Lombok annotations to automatically generate getters, setters,
 * a no-arguments constructor, and an all-arguments constructor.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeDTO {

    /**
     * The unique identifier for the room type.
     * This is used to distinguish between different room types in the system.
     */
    private Long id;

    /**
     * The name of the room type.
     * This represents the specific name of the room (e.g., "Single Room", "Deluxe Suite").
     */
    private String name;

    /**
     * The identifier of the contract associated with the room type.
     * This links the room type to a specific contract in the system.
     */
    private Long contractId;

    /**
     * The price per person for this room type.
     * Represents the cost for one person to stay in this room type.
     */
    private Double pricePerPerson;

    /**
     * The total number of rooms available for this room type.
     * Indicates how many rooms of this type are available in the hotel.
     */
    private Integer numberOfRooms;

    /**
     * The maximum number of adults allowed in this room type.
     * Specifies the upper limit of adult occupancy for this room type.
     */
    private Integer maxAdults;
}
