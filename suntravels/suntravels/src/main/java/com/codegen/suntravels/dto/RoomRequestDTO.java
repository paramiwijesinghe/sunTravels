package com.codegen.suntravels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) class for representing a room request.
 * It contains the number of adults for a room request.
 *
 * <p>This class is used to hold the data for requesting a room based on the number of adults.</p>
 *
 * <p>The class includes Lombok annotations to automatically generate getters, setters,
 * a no-arguments constructor, and an all-arguments constructor.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequestDTO {

    /**
     * The number of adults for the room request.
     * Represents the number of adults for whom the room is being requested.
     */
    private Integer numberOfAdults;

    /**
     * The number of rooms being requested for the given number of adults.
     */
    private Integer numberOfRooms;
}
