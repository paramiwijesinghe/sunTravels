package com.codegen.suntravels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing a contract.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDTO {
    /**
     * Unique identifier for the contract.
     */
    private Long id;

    /**
     * Identifier of the associated hotel.
     */
    private Long hotelId;

    /**
     * Name of the associated hotel.
     */
    private String hotelName;

    /**
     * Start date of the contract.
     */
    private LocalDate startDate;

    /**
     * End date of the contract.
     */
    private LocalDate endDate;

    /**
     * Markup percentage applied to contract rates.
     */
    private Double markupPercentage;

    /**
     * List of room types associated with this contract.
     */
    private List<RoomTypeDTO> roomTypes = new ArrayList<>();
}