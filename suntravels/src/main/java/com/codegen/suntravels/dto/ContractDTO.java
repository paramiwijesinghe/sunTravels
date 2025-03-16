package com.codegen.suntravels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDTO {
    private Long id;
    private Long hotelId;
    private String hotelName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double markupPercentage;
    private List<RoomTypeDTO> roomTypes = new ArrayList<>();
}
