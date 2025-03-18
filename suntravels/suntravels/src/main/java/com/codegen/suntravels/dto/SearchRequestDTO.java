package com.codegen.suntravels.dto;

import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestDTO {
    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "Check-in date cannot be in the past")
    private LocalDate checkInDate;

    @NotNull(message = "Number of nights is required")
    private Integer numberOfNights;

    @NotNull(message = "Room requests cannot be empty")
    private List<RoomRequestDTO> roomRequests;
}