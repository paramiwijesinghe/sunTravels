package com.codegen.suntravels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) class for representing the result of a room type query.
 * It contains the details about a room type, including its total price, availability,
 * maximum adult occupancy, and the number of available rooms.
 *
 * <p>This class is used to provide information about room type availability and pricing,
 * typically after querying a room type in the system.</p>
 *
 * <p>The class includes Lombok annotations to automatically generate getters, setters,
 * a no-arguments constructor, and an all-arguments constructor.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeResultDTO {

    /**
     * The unique identifier for the room type.
     * This is used to distinguish between different room types in the system.
     */
    private Long id;

    /**
     * The name of the room type.
     * This represents the specific name of the room (e.g., "Standard Room", "Luxury Suite").
     */
    private String name;

    /**
     * The total price for this room type.
     * Represents the cost for the room type, including any additional fees or taxes.
     */
    private Double totalPrice;

    /**
     * A boolean flag indicating whether the room type is available.
     * If true, the room type is available for booking; if false, it is not available.
     */
    private boolean available;

    /**
     * The maximum number of adults allowed in this room type.
     * Specifies the upper limit of adult occupancy for this room type.
     */
    private Integer maxAdults;

    /**
     * The number of rooms available for this room type.
     * Represents the total number of rooms of this type that are currently available.
     */
    private Integer availableRooms;
}
