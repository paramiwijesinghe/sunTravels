package com.codegen.suntravels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a contract expiry report.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractExpiryReportDTO {
    /**
     * Unique identifier for the contract.
     */
    private Long contractId;

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
     * Number of days remaining until the contract expires.
     */
    private Long daysToExpiry;
}