package com.codegen.suntravels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) class for representing a report request.
 * It contains the start and end dates for generating a report.
 *
 * <p>This class is used to hold the input data when making a request to generate reports
 * based on a specified date range.</p>
 *
 * <p>The class includes Lombok annotations to automatically generate getters, setters,
 * a no-arguments constructor, and an all-arguments constructor.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDTO {

    /**
     * The start date for the report generation.
     * Represents the beginning of the date range for the report.
     */
    private LocalDate fromDate;

    /**
     * The end date for the report generation.
     * Represents the end of the date range for the report.
     */
    private LocalDate toDate;
}
