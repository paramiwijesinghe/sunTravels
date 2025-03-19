package com.codegen.suntravels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) class for representing a room availability report.
 * It contains information about the availability of rooms in a hotel for a specific date.
 *
 * <p>This class is used to hold the data related to room availability, including hotel name,
 * room type, total rooms, available rooms, and the date of the report.</p>
 *
 * <p>The class includes Lombok annotations to automatically generate getters, setters,
 * a no-arguments constructor, and an all-arguments constructor.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvailabilityReportDTO {

    /**
     * The name of the hotel for which the room availability is being reported.
     */
    private String hotelName;

    /**
     * The name of the room type for which the availability is being reported.
     */
    private String roomTypeName;

    /**
     * The total number of rooms of the specified type in the hotel.
     */
    private Integer totalRooms;

    /**
     * The number of rooms of the specified type that are currently available.
     */
    private Integer availableRooms;

    /**
     * The date for which the room availability is being reported.
     */
    private LocalDate date;
}
