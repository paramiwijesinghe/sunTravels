package com.codegen.suntravels.dto;

import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) class for representing a search request.
 * It contains the necessary details for searching available rooms based on check-in date,
 * number of nights, and room requests.
 *
 * <p>This class is used to capture input data for searching available rooms, including
 * validation constraints for the check-in date, number of nights, and room requests.</p>
 *
 * <p>The class includes Lombok annotations to automatically generate getters, setters,
 * a no-arguments constructor, and an all-arguments constructor.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestDTO {

    /**
     * The check-in date for the room search.
     * This field must not be null and must not represent a past date.
     */
    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "Check-in date cannot be in the past")
    private LocalDate checkInDate;

    /**
     * The number of nights for the stay.
     * This field must not be null and specifies how long the stay will last.
     */
    @NotNull(message = "Number of nights is required")
    private Integer numberOfNights;

    /**
     * A list of room requests for the search.
     * This field must not be null and cannot be empty, as it specifies the room types and quantities requested.
     */
    @NotNull(message = "Room requests cannot be empty")
    private List<RoomRequestDTO> roomRequests;
}
